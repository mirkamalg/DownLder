package com.mirkamalg.data.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mirkamalg.data.dataSource.local.downloadHistory.DownloadHistoryDao
import com.mirkamalg.data.dataSource.local.downloadHistory.model.DownloadHistoryItem

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

@Database(entities = [DownloadHistoryItem::class], version = 1, exportSchema = false)
abstract class HistoryDataBase : RoomDatabase() {
    abstract val downloadHistoryItemsDao: DownloadHistoryDao
}