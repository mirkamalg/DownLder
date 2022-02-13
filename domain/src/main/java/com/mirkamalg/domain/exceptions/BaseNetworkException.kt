package com.mirkamalg.domain.exceptions

import androidx.annotation.StringRes

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

abstract class BaseNetworkException(
    val remoteMessage: String?,
    @StringRes val localMessageStringId: Int? = null
) : Exception(remoteMessage)