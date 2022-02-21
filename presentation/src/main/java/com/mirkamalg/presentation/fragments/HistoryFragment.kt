package com.mirkamalg.presentation.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.mirkamalg.domain.model.history.HistoryEntity
import com.mirkamalg.presentation.databinding.FragmentHistoryBinding
import com.mirkamalg.presentation.databinding.ItemHistoryBinding
import com.mirkamalg.presentation.utils.recycler_view.RecyclerListAdapter
import com.mirkamalg.presentation.viewmodel.HistoryEffect
import com.mirkamalg.presentation.viewmodel.HistoryState
import com.mirkamalg.presentation.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by Mirkamal Gasimov on 14.02.2022.
 */

class HistoryFragment :
    BaseFragment<FragmentHistoryBinding, HistoryState, HistoryEffect, HistoryViewModel>() {

    private val adapter by lazy {
        RecyclerListAdapter<ItemHistoryBinding, HistoryEntity>(
            onInflate = ItemHistoryBinding::inflate,
            onBind = { itemHistoryBinding, data ->
                itemHistoryBinding.apply {
                    textView.text = data.title
                }
            }
        )
    }

    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate
    override val onBind: FragmentHistoryBinding.() -> Unit
        get() = {
            recyclerView.adapter = adapter
            root.setOnRefreshListener {
                viewModel.getAll(true)
            }
        }
    override val viewModel: HistoryViewModel by viewModel()

    override fun HistoryViewModel.onObserve() {
        state.observe(viewLifecycleOwner, ::syncState)
        effect.observe(viewLifecycleOwner) {

        }
    }

    private fun syncState(state: HistoryState) {
        Timber.e("STATE: $state")
        binding?.apply {
            progressBar.isVisible = state.isLoading
            adapter.submitList(state.entities)
            root.isRefreshing = state.isRefreshing
        }
    }
}