package com.mirkamalg.presentation.di

import com.mirkamalg.presentation.viewmodel.ConversionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Mirkamal Gasimov on 6.02.2022.
 */

val presentationModule = module {

    viewModel {
        ConversionViewModel(
            getVideoDataUseCase = get()
        )
    }

}