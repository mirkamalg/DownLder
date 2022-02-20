package com.mirkamalg.domain.model.conversion

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

@Parcelize
data class VideoMetaDataEntity(
    val title: String,
    val description: String,
    val channel: String,
    val length: String,
    val thumbnailUrl: String,
    val viewCount: String,
    val likeCount: String,
    val videoId: String
) : Parcelable
