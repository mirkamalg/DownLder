package com.mirkamalg.data.dataSource.local.downloadHistory

import com.mirkamalg.data.dataSource.local.downloadHistory.model.DownloadHistoryItem

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

interface DownloadHistoryLocalDataSource {

    fun getAll(): List<DownloadHistoryItem>

    fun insert(new: DownloadHistoryItem): Long

}