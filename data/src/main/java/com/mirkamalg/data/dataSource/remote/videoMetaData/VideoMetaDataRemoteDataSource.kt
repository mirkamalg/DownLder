package com.mirkamalg.data.dataSource.remote.videoMetaData

import com.mirkamalg.data.dataSource.remote.videoMetaData.model.VideoMetaDataDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

interface VideoMetaDataRemoteDataSource {

    @GET("videos")
    suspend fun getVideoMetaData(
        @Query("id") videoId: String,
        @Query("part") parts: String
    ): VideoMetaDataDto

}