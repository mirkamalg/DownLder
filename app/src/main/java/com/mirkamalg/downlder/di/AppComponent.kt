package com.mirkamalg.downlder.di

import com.mirkamalg.data.di.dataModule
import com.mirkamalg.domain.di.domainModule
import com.mirkamalg.presentation.di.presentationModule

/**
 * Created by Mirkamal Gasimov on 6.02.2022.
 */

val appModules = listOf(
    presentationModule,
    domainModule,
    dataModule
)