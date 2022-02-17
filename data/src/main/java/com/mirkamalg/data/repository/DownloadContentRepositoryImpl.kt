package com.mirkamalg.data.repository

import com.mirkamalg.data.dataSource.remote.downloadHtmlPage.DownloadContentRemoteDataSource
import com.mirkamalg.domain.repository.DownloadContentRepository
import org.jsoup.nodes.Document

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

class DownloadContentRepositoryImpl(
    private val downloadContentRemoteDataSource: DownloadContentRemoteDataSource
) : DownloadContentRepository {

    override suspend fun getDownloadHtmlPage(type: String, videoId: String): Document =
        downloadContentRemoteDataSource.getDownloadHtmlPage(
            type, videoId
        )
}