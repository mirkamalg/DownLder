package com.mirkamalg.domain.repository

import org.jsoup.nodes.Document

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

interface DownloadContentRepository : BaseRepository {

    suspend fun getDownloadHtmlPage(
        type: String,
        videoId: String
    ): Document

}