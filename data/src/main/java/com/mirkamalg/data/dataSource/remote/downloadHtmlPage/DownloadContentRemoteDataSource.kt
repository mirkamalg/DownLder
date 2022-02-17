package com.mirkamalg.data.dataSource.remote.downloadHtmlPage

import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

interface DownloadContentRemoteDataSource {

    @GET("button/{type}/{id}")
    suspend fun getDownloadHtmlPage(
        @Path("type") type: String,
        @Path("id") videoId: String
    ): Document

}