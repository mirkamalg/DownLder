package com.mirkamalg.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import com.mirkamalg.domain.utils.Regexes

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionViewModel(
    private val getVideoDataUseCase: ConversionUseCase.GetVideoDataUseCase,
    private val downloadContentPageUseCase: ConversionUseCase.DownloadContentPageUseCase,
    private val startContentDownloadUseCase: ConversionUseCase.StartContentDownloadUseCase
) : BaseViewModel<ConversionState, ConversionEffect>() {

    init {
        postState(ConversionState())
    }

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

    fun openDownloadPage(context: Context) {
        state.value?.searchedUrl?.let { youtubeUrl ->
            extractVideoIdFromUrl(youtubeUrl)?.let { videoId ->
                CustomTabsIntent.Builder().build().launchUrl(
                    context, Uri.parse(
                        videoIdToDownloadPageUrl(videoId)
                    )
                )
            }
        }
    }

    fun downloadContent(
        type: ConversionUseCase.DownloadType,
    ) {
        launch(
            downloadContentPageUseCase,
            ConversionUseCase.DownloadContentPageUseCase.GetDownloadHtmlPageParams(
                type,
                state.value?.videoMetaDataEntity?.videoId.toString()
            )
        ) {
            onSuccess = {
                launch(
                    startContentDownloadUseCase,
                    ConversionUseCase.StartContentDownloadUseCase.StartContentDownloadParams(
                        type,
                        it,
                        state.value?.videoMetaDataEntity?.title.toString()
                    )
                )
            }
        }
    }

    private fun extractVideoIdFromUrl(videoUrl: String) =
        Regexes.youtubeUrlRegex.matchEntire(videoUrl)?.groups?.findLast {
            it?.value?.length == 11
        }?.value

    private fun videoIdToDownloadPageUrl(videoId: String) =
        "https://api.vevioz.com/@api/button/mp3/$videoId"

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