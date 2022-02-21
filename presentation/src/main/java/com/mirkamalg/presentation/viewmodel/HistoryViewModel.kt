package com.mirkamalg.presentation.viewmodel

import com.mirkamalg.domain.model.history.HistoryEntity
import com.mirkamalg.domain.usecase.history.HistoryUseCase

/**
 * Created by Mirkamal Gasimov on 14.02.2022.
 */

class HistoryViewModel(
    private val loadHistoryUseCase: HistoryUseCase.LoadHistoryUseCase
) : BaseViewModel<HistoryState, HistoryEffect>() {

    init {
        postState(HistoryState())
        getAll()
    }

    fun getAll(isRefreshing: Boolean = false) {
        launch(loadHistoryUseCase, Unit) {
            onStart = {
                if (isRefreshing) {
                    state.value?.copy(
                        isRefreshing = true
                    )?.let { setState(it) }
                }
            }
            onSuccess = {
                postState(
                    HistoryState(isLoading = false, isRefreshing = false, it)
                )
            }
        }
    }

}

data class HistoryState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val entities: List<HistoryEntity> = emptyList()
)

class HistoryEffect