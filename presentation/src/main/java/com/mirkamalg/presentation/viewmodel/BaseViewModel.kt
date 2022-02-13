package com.mirkamalg.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirkamalg.domain.usecase.BaseUseCase
import com.mirkamalg.domain.usecase.RequestLifecycleBlock
import com.mirkamalg.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

abstract class BaseViewModel<S, E> : ViewModel() {

    private val _state = MutableLiveData<S>()
    val state: LiveData<S> = _state

    private val _effect = SingleLiveEvent<E>()
    val effect: LiveData<E> = _effect

    @SuppressLint("NullSafeMutableLiveData")
    fun postState(state: S) = _state.postValue(state)

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
                onCancel = {
                    request.onCancel?.invoke(it)
                }
                onTerminate = {
                    request.onTerminate
                }
                onError = {
                    Timber.e(it)
                    request.onError?.invoke(it) ?: handleError(it)
                }
            }
            useCase.execute(input, requestLifecycleBlock)
        }
    }

    private fun handleError(t: Throwable) {

    }

}