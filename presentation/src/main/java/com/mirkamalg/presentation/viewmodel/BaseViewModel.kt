package com.mirkamalg.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirkamalg.domain.exceptions.*
import com.mirkamalg.domain.usecase.BaseUseCase
import com.mirkamalg.domain.usecase.CommonEffect
import com.mirkamalg.domain.usecase.RequestLifecycleBlock
import com.mirkamalg.domain.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

abstract class BaseViewModel<S, E> : ViewModel() {

    private val _state = MutableLiveData<S>()
    val state: LiveData<S> = _state

    private val _effect = SingleLiveEvent<E>()
    val effect: LiveData<E> = _effect

    private val _commonEffect = SingleLiveEvent<CommonEffect>()
    val commonEffect: LiveData<CommonEffect> = _commonEffect

    @SuppressLint("NullSafeMutableLiveData")
    fun postState(state: S) = _state.postValue(state)

    /**
     * For immediate state updates
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun setState(state: S) {
        _state.value = state
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun postEffect(effect: E) = _effect.postValue(effect)

    protected fun <I, O, U : BaseUseCase<I, O>> launch(
        useCase: U,
        input: I,
        block: RequestLifecycleBlock<O> = {}
    ) {
        viewModelScope.launch {
            val request = BaseUseCase.Request<O>().apply(block)

            val requestLifecycleBlock: RequestLifecycleBlock<O> = {
                onStart = {
                    request.onStart?.invoke()
                }
                onSuccess = {
                    request.onSuccess(it)
                }
                onCompletion = {
                    request.onCompletion
                }
                onError = {
                    Timber.e(it)
                    request.onError?.invoke(it) ?: handleError(it.toActualException())
                }
            }
            useCase.execute(input, requestLifecycleBlock)
        }
    }

    private fun handleError(t: Throwable) {
        when (t) {
            is ConnectionException -> {
                _commonEffect.postValue(CommonEffect.ConnectionExceptionEffect)
            }
            is ServerException -> {
                _commonEffect.postValue(CommonEffect.ServerExceptionEffect)
            }
            is UnauthorizedException -> {
                _commonEffect.postValue(CommonEffect.UnauthorizedExceptionEffect)
            }
            is UnknownException -> {
                _commonEffect.postValue(CommonEffect.UnknownExceptionEffect)
            }
        }
    }

    private fun Throwable.toActualException() = when (this) {
        is SocketException,
        is SocketTimeoutException,
        is UnknownHostException,
        -> ConnectionException
        is BaseNetworkException -> this
        else -> UnknownException
    }

}