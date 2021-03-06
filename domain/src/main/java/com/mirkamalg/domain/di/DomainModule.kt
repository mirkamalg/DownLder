package com.mirkamalg.domain.di

import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

/**
 * Created by Mirkamal Gasimov on 6.02.2022.
 */

const val IO_DISPATCHER = "IO_DISPATCHER"

val domainModule = module {

    single<CoroutineContext>(named(IO_DISPATCHER)) { Dispatchers.IO }

    factory {
        ConversionUseCase.GetVideoDataUseCase(
            coroutineContext = get(named(IO_DISPATCHER)),
            videoMetaDataRepository = get()
        )
    }

}