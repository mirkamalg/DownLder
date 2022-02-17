package com.mirkamalg.data.dataSource.remote.videoMetaData.model

import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.squareup.moshi.JsonClass

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

@JsonClass(generateAdapter = true)
data class VideoMetaDataDto(
    val kind: String? = null,
    val etag: String? = null,
    val items: List<Item>? = null,
    val pageInfo: PageInfo? = null
) {
    fun entity(videoId: String): VideoMetaDataEntity {
        val snippet = items?.get(0)?.snippet
        val contentDetails = items?.get(0)?.contentDetails
        val stats = items?.get(0)?.statistics
        return VideoMetaDataEntity(
            snippet?.title.toString(),
            snippet?.description.toString(),
            snippet?.channelTitle.toString(),
            contentDetails?.duration?.replace("PS", "", true).toString(),
            snippet?.thumbnails?.high?.url.toString(),
            stats?.viewCount.toString(),
            stats?.likeCount.toString(),
            videoId
        )
    }
}

@JsonClass(generateAdapter = true)
data class Item(
    val kind: String? = null,
    val etag: String? = null,
    val id: String? = null,
    val snippet: Snippet? = null,
    val contentDetails: ContentDetails? = null,
    val statistics: Statistics? = null
)

@JsonClass(generateAdapter = true)
data class ContentDetails(
    val duration: String? = null,
    val dimension: String? = null,
    val definition: String? = null,
    val caption: String? = null,
    val licensedContent: Boolean? = null,
    val projection: String? = null
)

@JsonClass(generateAdapter = true)
data class Snippet(
    val publishedAt: String? = null,
    val channelId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val thumbnails: Thumbnails? = null,
    val channelTitle: String? = null,
    val tags: List<String>? = null,
    val categoryId: String? = null,
    val liveBroadcastContent: String? = null,
    val defaultLanguage: String? = null,
    val localized: Localized? = null
)

@JsonClass(generateAdapter = true)
data class Localized(
    val title: String? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
data class Thumbnails(
    val default: Default? = null,
    val medium: Default? = null,
    val high: Default? = null
)

@JsonClass(generateAdapter = true)
data class Default(
    val url: String? = null,
    val width: Long? = null,
    val height: Long? = null
)

@JsonClass(generateAdapter = true)
data class Statistics(
    val viewCount: String? = null,
    val likeCount: String? = null,
    val favoriteCount: String? = null,
    val commentCount: String? = null
)

@JsonClass(generateAdapter = true)
data class PageInfo(
    val totalResults: Long? = null,
    val resultsPerPage: Long? = null
)

