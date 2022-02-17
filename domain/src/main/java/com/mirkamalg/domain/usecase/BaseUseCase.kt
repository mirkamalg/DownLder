package com.mirkamalg.domain.usecase

import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

typealias RequestLifecycleBlock<T> = BaseUseCase.Request<T>.() -> Unit

abstract class BaseUseCase<I, O>(
    private val coroutineContext: CoroutineContext
) {

    abstract suspend fun onExecute(input: I): O

    suspend fun execute(input: I, block: RequestLifecycleBlock<O> = {}) {
        val request = Request<O>().apply(block).also { it.onStart?.invoke() }
        flow {
            emit(onExecute(input))
        }.flowOn(coroutineContext).catch {
            request.onError?.invoke(it)
        }.onCompletion {
            request.onCompletion?.invoke()
        }.collect {
            request.onSuccess(it)
        }
    }

    class Request<T> {
        var onSuccess: (T) -> Unit = {}
        var onStart: (() -> Unit)? = null
        var onError: ((Throwable) -> Unit)? = null
        var onCompletion: (() -> Unit)? = null
    }
}