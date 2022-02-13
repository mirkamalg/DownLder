package com.mirkamalg.domain.repository

import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

interface VideoMetaDataRepository : BaseRepository {

    suspend fun getVideoMetaData(
        videoId: String,
        parts: String
    ): VideoMetaDataEntity

}