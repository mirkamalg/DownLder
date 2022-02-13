package com.mirkamalg.presentation.viewmodel

import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionViewModel(
    private val getVideoDataUseCase: ConversionUseCase.GetVideoDataUseCase
) : BaseViewModel<ConversionState, ConversionEffect>() {

    init {
        postState(ConversionState())
    }

    fun getVideoMetaData(videoId: String) {
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
}