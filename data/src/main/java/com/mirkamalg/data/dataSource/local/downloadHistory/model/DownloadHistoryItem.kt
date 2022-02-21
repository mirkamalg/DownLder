package com.mirkamalg.data.dataSource.local.downloadHistory.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mirkamalg.domain.model.history.HistoryEntity
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

@Entity(tableName = "downloads")
data class DownloadHistoryItem(
    val title: String,
    val description: String,
    val channel: String,
    val length: String,
    val thumbnailUrl: String,
    val viewCount: String,
    val likeCount: String,
    val videoId: String,
    val type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    val entity: HistoryEntity
        get() = HistoryEntity(
            title,
            description,
            length,
            thumbnailUrl,
            videoId,
            ConversionUseCase.DownloadType.from(type)
        )
}
