package com.mirkamalg.domain.model.conversion

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

data class VideoMetaDataEntity(
    val title: String,
    val description: String,
    val channel: String,
    val length: String,
    val thumbnailUrl: String,
    val viewCount: String,
    val likeCount: String,
)
