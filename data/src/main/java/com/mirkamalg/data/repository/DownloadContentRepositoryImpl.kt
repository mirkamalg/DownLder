package com.mirkamalg.data.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.mirkamalg.data.dataSource.remote.downloadHtmlPage.DownloadContentRemoteDataSource
import com.mirkamalg.domain.repository.DownloadContentRepository
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import org.jsoup.nodes.Document
import timber.log.Timber
import java.net.URLEncoder

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

    private val downloadBaseUrl = "https://api.vevioz.com/download/"

    override suspend fun startContentDownload(
        params: ConversionUseCase.StartContentDownloadUseCase.StartContentDownloadParams,
        context: Context
    ) {
        val url = params.document.getDownloadUrl(params.type, params.videoTitle)

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle(
                "${params.videoTitle}${getFileFormat(params.type)}"
            )
            setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "DownLder/${params.videoTitle}${getFileFormat(params.type)}"
            )
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        val manager = context.getSystemService(DownloadManager::class.java)
        Timber.e("Enqueueing for download: $url")
        manager.enqueue(request)
    }

    private fun Document.getDownloadUrl(
        type: ConversionUseCase.DownloadType,
        title: String
    ): String {
        val downloadElements = getElementsByAttributeValueMatching(
            if (type == ConversionUseCase.DownloadType.MP3) {
                "data-mp3-bitrate"
            } else "data-mp4-note",
            if (type == ConversionUseCase.DownloadType.MP3) {
                "256"
            } else "1080p"
        )
        val downloadTag = downloadElements[0].attr(
            if (type == ConversionUseCase.DownloadType.MP3) {
                "data-mp3-tag"
            } else "data-mp4-tag"
        )
        val videoTitleEncoded = URLEncoder.encode(title, "utf-8")

        return "$downloadBaseUrl${downloadTag}${
            if (type == ConversionUseCase.DownloadType.MP3) {
                "$videoTitleEncoded.mp3"
            } else "$videoTitleEncoded.mp4"
        }"
    }

    private fun getFileFormat(type: ConversionUseCase.DownloadType) =
        if (type == ConversionUseCase.DownloadType.MP3) {
            ".mp3"
        } else ".mp4"
}