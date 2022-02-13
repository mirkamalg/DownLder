package com.mirkamalg.domain.usecase.conversion

import com.mirkamalg.domain.enums.MetaDataParts
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.repository.VideoMetaDataRepository
import com.mirkamalg.domain.usecase.BaseUseCase
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

    //TODO add other use cases

}