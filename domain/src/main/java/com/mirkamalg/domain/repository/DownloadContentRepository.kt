package com.mirkamalg.domain.repository

import android.content.Context
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import org.jsoup.nodes.Document

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

interface DownloadContentRepository : BaseRepository {

    suspend fun getDownloadHtmlPage(
        type: String,
        videoId: String
    ): Document

    suspend fun startContentDownload(
        params: ConversionUseCase.StartContentDownloadUseCase.StartContentDownloadParams,
        context: Context
    )

}