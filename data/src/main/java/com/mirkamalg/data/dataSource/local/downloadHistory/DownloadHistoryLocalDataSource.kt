package com.mirkamalg.data.dataSource.local.downloadHistory

import com.mirkamalg.data.dataSource.local.downloadHistory.model.DownloadHistoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

interface DownloadHistoryLocalDataSource {

    fun getAll(): Flow<List<DownloadHistoryItem>>

    fun insert(new: DownloadHistoryItem): Long

}