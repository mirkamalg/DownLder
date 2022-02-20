package com.mirkamalg.data.dataSource.local.downloadHistory

import com.mirkamalg.data.dataSource.local.HistoryDataBase
import com.mirkamalg.data.dataSource.local.downloadHistory.model.DownloadHistoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

class DownloadHistoryLocalDataSourceImpl(
    db: HistoryDataBase
) : DownloadHistoryLocalDataSource {

    private val downloadHistoryDao = db.downloadHistoryItemsDao

    override fun getAll(): Flow<List<DownloadHistoryItem>> = downloadHistoryDao.getAll()

    override fun insert(new: DownloadHistoryItem): Long = downloadHistoryDao.insert(new)

}