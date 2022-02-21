package com.mirkamalg.domain.model.history

import com.mirkamalg.domain.usecase.conversion.ConversionUseCase

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

data class HistoryEntity(
    val title: String,
    val description: String,
    val length: String,
    val thumbnailUrl: String,
    val videoId: String,
    val type: ConversionUseCase.DownloadType
)
