package com.mirkamalg.domain.usecase

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

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
        try {
            val result = withContext(coroutineContext) { onExecute(input) }
            request.onSuccess(result)
        } catch (e: CancellationException) {
            request.onCancel?.invoke(e)
        } catch (e: Throwable) {
            request.onError?.invoke(e)
        } finally {
            request.onTerminate?.invoke()
        }
    }

    class Request<T> {
        var onSuccess: (T) -> Unit = {}
        var onStart: (() -> Unit)? = null
        var onError: ((Throwable) -> Unit)? = null
        var onCancel: ((CancellationException) -> Unit)? = null
        var onTerminate: (() -> Unit)? = null
    }
}