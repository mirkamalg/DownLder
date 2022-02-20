package com.mirkamalg.domain.usecase.conversion

import android.content.Context
import androidx.annotation.StringRes
import com.mirkamalg.domain.R
import com.mirkamalg.domain.enums.MetaDataParts
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.repository.DownloadContentRepository
import com.mirkamalg.domain.repository.DownloadHistoryRepository
import com.mirkamalg.domain.repository.VideoMetaDataRepository
import com.mirkamalg.domain.usecase.BaseUseCase
import org.jsoup.nodes.Document
import kotlin.coroutines.CoroutineContext

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionUseCase {

    enum class DownloadType(val type: String, @StringRes val strId: Int) {
        MP3("mp3", R.string.msg_mp3), VIDEO("videos", R.string.msg_video)
    }

    class GetVideoDataUseCase(
        coroutineContext: CoroutineContext,
        private val videoMetaDataRepository: VideoMetaDataRepository
    ) : BaseUseCase<String, VideoMetaDataEntity>(coroutineContext) {

        override suspend fun onExecute(input: String): VideoMetaDataEntity =
            videoMetaDataRepository.getVideoMetaData(
                input,
                MetaDataParts.values().joinToString(",") { it.part }
            )
    }

    class DownloadContentPageUseCase(
        coroutineContext: CoroutineContext,
        private val downloadContentRepository: DownloadContentRepository
    ) : BaseUseCase<DownloadContentPageUseCase.GetDownloadHtmlPageParams, Document>(coroutineContext) {

        data class GetDownloadHtmlPageParams(
            val type: DownloadType,
            val videoId: String
        )

        override suspend fun onExecute(input: GetDownloadHtmlPageParams): Document {
            return downloadContentRepository.getDownloadHtmlPage(
                input.type.type,
                input.videoId
            )
        }

    }

    class StartContentDownloadUseCase(
        coroutineContext: CoroutineContext,
        private val downloadContentRepository: DownloadContentRepository,
        private val context: Context
    ) : BaseUseCase<StartContentDownloadUseCase.StartContentDownloadParams, Unit>(coroutineContext) {

        data class StartContentDownloadParams(
            val type: DownloadType,
            val document: Document,
            val videoData: VideoMetaDataEntity
        )

        override suspend fun onExecute(input: StartContentDownloadParams) {
            downloadContentRepository.startContentDownload(input, context)
        }

    }

    class InsertNewDownloadUseCase(
        coroutineContext: CoroutineContext,
        private val downloadHistoryRepository: DownloadHistoryRepository
    ) : BaseUseCase<VideoMetaDataEntity, Long>(coroutineContext) {

        override suspend fun onExecute(input: VideoMetaDataEntity): Long {
            input.apply {
                return downloadHistoryRepository.insert(
                    title, description, channel, length, thumbnailUrl, viewCount, likeCount, videoId
                )
            }
        }

    }

}