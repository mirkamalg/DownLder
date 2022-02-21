package com.mirkamalg.domain.usecase.history

import com.mirkamalg.domain.model.history.HistoryEntity
import com.mirkamalg.domain.repository.DownloadHistoryRepository
import com.mirkamalg.domain.usecase.BaseUseCase
import kotlin.coroutines.CoroutineContext

/**
 * Created by Mirkamal Gasimov on 21.02.2022.
 */

class HistoryUseCase {

    class LoadHistoryUseCase(
        coroutineContext: CoroutineContext,
        private val downloadHistoryRepository: DownloadHistoryRepository
    ) : BaseUseCase<Unit, List<HistoryEntity>>(coroutineContext) {

        override suspend fun onExecute(input: Unit) = downloadHistoryRepository.getAll()

    }

}