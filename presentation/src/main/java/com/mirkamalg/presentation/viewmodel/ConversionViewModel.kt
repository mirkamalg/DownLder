package com.mirkamalg.presentation.viewmodel

import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mirkamalg.domain.broadcast_receivers.AddToHistoryReceiver
import com.mirkamalg.domain.broadcast_receivers.FileDownloadCompletedReceiver
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import com.mirkamalg.domain.utils.Regexes
import com.mirkamalg.domain.utils.SingleLiveEvent
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionViewModel(
    private val getVideoDataUseCase: ConversionUseCase.GetVideoDataUseCase,
    private val downloadContentPageUseCase: ConversionUseCase.DownloadContentPageUseCase,
    private val startContentDownloadUseCase: ConversionUseCase.StartContentDownloadUseCase,
    private val insertNewDownloadUseCase: ConversionUseCase.InsertNewDownloadUseCase
) : BaseViewModel<ConversionState, ConversionEffect>() {

    init {
        postState(ConversionState())
    }

    private val _addToHistoryTrigger = SingleLiveEvent<VideoMetaDataEntity>()
    val addToHistoryTrigger: LiveData<VideoMetaDataEntity> = _addToHistoryTrigger

    fun getVideoMetaData(videoUrl: String) {
        fun postInvalidUrlEffect() = postEffect(ConversionEffect.InvalidUrl)

        state.value?.let {
            val new = it.copy(searchedUrl = videoUrl)
            setState(new)
        }
        if (Regexes.youtubeUrlRegex.matches(videoUrl).not()) {
            postInvalidUrlEffect()
            return
        }
        val videoId = extractVideoIdFromUrl(videoUrl)

        if (videoId == null) {
            postInvalidUrlEffect()
            return
        }

        launch(getVideoDataUseCase, videoId) {
            onStart = {
                state.value?.let {
                    postState(it.copy(loading = true))
                }
            }
            onSuccess = { entity ->
                state.value?.let {
                    postState(
                        it.copy(
                            loading = false,
                            videoMetaDataEntity = entity,
                            searchedUrl = videoUrl
                        )
                    )
                }
            }
            onError = { t ->
                state.value?.let {
                    postState(
                        it.copy(
                            loading = false,
                            videoMetaDataEntity = null,
                            searchedUrl = ""
                        )
                    )
                    postEffect(ConversionEffect.Error(t.message))
                }
            }
        }
    }

    fun downloadContent(
        type: ConversionUseCase.DownloadType,
    ) {
        state.value?.videoMetaDataEntity?.let {
            launch(
                downloadContentPageUseCase,
                ConversionUseCase.DownloadContentPageUseCase.GetDownloadHtmlPageParams(
                    type,
                    state.value?.videoMetaDataEntity?.videoId.toString()
                )
            ) {
                onSuccess = { document ->
                    launch(
                        startContentDownloadUseCase,
                        ConversionUseCase.StartContentDownloadUseCase.StartContentDownloadParams(
                            type,
                            document,
                            it
                        )
                    )
                }
            }
        }
    }

    fun addToHistory(entity: VideoMetaDataEntity) {
        launch(insertNewDownloadUseCase, entity) {
            onSuccess = {
                Timber.d("Inserted successfully with id = $it => $entity")
            }
            onError = {
                Timber.e(it)
            }
        }
    }

    private fun extractVideoIdFromUrl(videoUrl: String) =
        Regexes.youtubeUrlRegex.matchEntire(videoUrl)?.groups?.findLast {
            it?.value?.length == 11
        }?.value

    fun startListeningForCompletedDownloads(context: Context) {
        val receiver: AddToHistoryReceiver by inject(AddToHistoryReceiver::class.java) {
            parametersOf(_addToHistoryTrigger)
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(
            receiver,
            IntentFilter(FileDownloadCompletedReceiver.ACTION_ADD_TO_HISTORY)
        )
    }

}

data class ConversionState(
    val loading: Boolean = false,
    val videoMetaDataEntity: VideoMetaDataEntity? = null,
    val searchedUrl: String = ""
)

sealed class ConversionEffect {
    data class Error(val message: String?) : ConversionEffect()
    object InvalidUrl : ConversionEffect()
}