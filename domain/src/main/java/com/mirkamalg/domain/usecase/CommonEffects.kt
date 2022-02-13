package com.mirkamalg.domain.usecase

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

sealed class CommonEffect {
    object ConnectionExceptionEffect : CommonEffect()
    object UnknownExceptionEffect : CommonEffect()
    object ServerExceptionEffect : CommonEffect()
    object UnauthorizedExceptionEffect : CommonEffect()
}