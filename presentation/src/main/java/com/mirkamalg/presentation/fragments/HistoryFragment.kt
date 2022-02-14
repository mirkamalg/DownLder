package com.mirkamalg.presentation.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mirkamalg.presentation.databinding.FragmentHistoryBinding
import com.mirkamalg.presentation.viewmodel.HistoryEffect
import com.mirkamalg.presentation.viewmodel.HistoryState
import com.mirkamalg.presentation.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Mirkamal Gasimov on 14.02.2022.
 */

class HistoryFragment :
    BaseFragment<FragmentHistoryBinding, HistoryState, HistoryEffect, HistoryViewModel>() {

    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate
    override val onBind: FragmentHistoryBinding.() -> Unit
        get() = {

        }
    override val viewModel: HistoryViewModel by viewModel()

    override fun HistoryViewModel.onObserve() {

    }
}