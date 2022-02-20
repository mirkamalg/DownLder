package com.mirkamalg.domain.model.history

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

data class HistoryEntity(
    val title: String,
    val description: String,
    val length: String,
    val thumbnailUrl: String,
    val videoId: String
)
