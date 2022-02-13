package com.mirkamalg.data.repository

import com.mirkamalg.data.dataSource.remote.downloadHtmlPage.DownloadHtmlPageRemoteDataSource
import com.mirkamalg.domain.repository.DownloadHtmlPageRepository
import org.jsoup.nodes.Document

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

class DownloadHtmlPageRepositoryImpl(
    private val downloadHtmlPageRemoteDataSource: DownloadHtmlPageRemoteDataSource
) : DownloadHtmlPageRepository {

    override suspend fun getDownloadHtmlPage(type: String, videoId: String): Document =
        downloadHtmlPageRemoteDataSource.getDownloadHtmlPage(
            type, videoId
        )
}