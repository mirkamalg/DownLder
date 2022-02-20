package com.mirkamalg.domain.repository

import com.mirkamalg.domain.model.history.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

interface DownloadHistoryRepository : BaseRepository {

    fun getAll(): Flow<List<HistoryEntity>>

    fun insert(
        title: String,
        description: String,
        channel: String,
        length: String,
        thumbnailUrl: String,
        viewCount: String,
        likeCount: String,
        videoId: String
    ): Long

}