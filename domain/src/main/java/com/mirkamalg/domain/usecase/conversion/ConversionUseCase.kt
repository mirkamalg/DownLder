package com.mirkamalg.domain.usecase.conversion

import com.mirkamalg.domain.enums.MetaDataParts
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.repository.DownloadContentRepository
import com.mirkamalg.domain.repository.VideoMetaDataRepository
import com.mirkamalg.domain.usecase.BaseUseCase
import org.jsoup.nodes.Document
import kotlin.coroutines.CoroutineContext

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionUseCase {

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

        enum class DownloadType(val type: String) {
            MP3("mp3"), VIDEO("videos")
        }

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

}