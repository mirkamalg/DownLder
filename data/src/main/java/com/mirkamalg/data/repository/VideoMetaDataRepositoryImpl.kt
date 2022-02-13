package com.mirkamalg.data.repository

import com.mirkamalg.data.dataSource.remote.videoMetaData.VideoMetaDataRemoteDataSource
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.repository.VideoMetaDataRepository

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class VideoMetaDataRepositoryImpl(private val videoMetaDataRemoteDataSource: VideoMetaDataRemoteDataSource) :
    VideoMetaDataRepository {

    override suspend fun getVideoMetaData(videoId: String, parts: String): VideoMetaDataEntity {
        return handleResponse(videoMetaDataRemoteDataSource.getVideoMetaData(videoId, parts)).entity
    }


}