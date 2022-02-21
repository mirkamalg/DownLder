package com.mirkamalg.data.repository

import com.mirkamalg.data.dataSource.local.downloadHistory.DownloadHistoryLocalDataSource
import com.mirkamalg.data.dataSource.local.downloadHistory.model.DownloadHistoryItem
import com.mirkamalg.domain.model.history.HistoryEntity
import com.mirkamalg.domain.repository.DownloadHistoryRepository

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

class DownloadHistoryRepositoryImpl(
    private val downloadHistoryLocalDataSource: DownloadHistoryLocalDataSource
) : DownloadHistoryRepository {

    override fun getAll(): List<HistoryEntity> =
        downloadHistoryLocalDataSource.getAll().map {
            it.entity
        }

    override fun insert(
        title: String,
        description: String,
        channel: String,
        length: String,
        thumbnailUrl: String,
        viewCount: String,
        likeCount: String,
        videoId: String,
        type: String
    ) = downloadHistoryLocalDataSource.insert(
        DownloadHistoryItem(
            title,
            description,
            channel,
            length,
            thumbnailUrl,
            viewCount,
            likeCount,
            videoId,
            type
        )
    )
}