package com.mirkamalg.data.dataSource.local.downloadHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mirkamalg.data.dataSource.local.downloadHistory.model.DownloadHistoryItem

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

@Dao
interface DownloadHistoryDao {

    @Query("SELECT * FROM downloads")
    fun getAll(): List<DownloadHistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(new: DownloadHistoryItem): Long

}