package com.mirkamalg.presentation.viewmodel

import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import com.mirkamalg.domain.utils.Regexes

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionViewModel(
    private val getVideoDataUseCase: ConversionUseCase.GetVideoDataUseCase
) : BaseViewModel<ConversionState, ConversionEffect>() {

    init {
        postState(ConversionState())
    }

    fun getVideoMetaData(videoUrl: String) {
        fun postInvalidUrlEffect() = postEffect(ConversionEffect.InvalidUrl)

        if (Regexes.youtubeUrlRegex.matches(videoUrl).not()) {
            postInvalidUrlEffect()
            return
        }
        val videoId =
            Regexes.youtubeUrlRegex.matchEntire(videoUrl)?.groups?.findLast {
                it?.value?.length == 11
            }?.value
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
                    postState(it.copy(loading = false, videoMetaDataEntity = entity))
                }
            }
            onError = { t ->
                state.value?.let {
                    postState(it.copy(loading = false, videoMetaDataEntity = null))
                    postEffect(ConversionEffect.Error(t.message))
                }
            }
        }
    }

}

data class ConversionState(
    var loading: Boolean = false,
    var videoMetaDataEntity: VideoMetaDataEntity? = null
)

sealed class ConversionEffect {
    data class Error(val message: String?) : ConversionEffect()
    object InvalidUrl : ConversionEffect()
}