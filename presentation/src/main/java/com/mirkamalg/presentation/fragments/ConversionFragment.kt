package com.mirkamalg.presentation.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.mirkamalg.presentation.databinding.FragmentConversionBinding
import com.mirkamalg.presentation.viewmodel.ConversionEffect
import com.mirkamalg.presentation.viewmodel.ConversionState
import com.mirkamalg.presentation.viewmodel.ConversionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionFragment :
    BaseFragment<FragmentConversionBinding, ConversionState, ConversionEffect, ConversionViewModel>() {

    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentConversionBinding
        get() = FragmentConversionBinding::inflate
    override val onBind: FragmentConversionBinding.() -> Unit
        get() = {
            viewModel.getVideoMetaData("3O1_3zBUKM8")

            textViewTest.setOnClickListener {
                viewModel.getVideoMetaData("3O1_3zBUKM8")
            }
        }
    override val viewModel: ConversionViewModel by viewModel()

    override fun ConversionViewModel.onObserve() {
        binding?.apply {
            state.observe(viewLifecycleOwner) {
                it?.apply {
                    progressBar.isVisible = loading
                    textViewTest.isVisible = videoMetaDataEntity != null
                    textViewTest.text = videoMetaDataEntity.toString()
                }
            }
            effect.observe(viewLifecycleOwner) {
                when (it) {
                    is ConversionEffect.Error -> {
                        Snackbar.make(textViewTest, it.message.toString(), LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}