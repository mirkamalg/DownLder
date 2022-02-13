package com.mirkamalg.domain.exceptions

import com.mirkamalg.domain.R

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

object ConnectionException :
    BaseNetworkException(null, R.string.err_connection_error_occurred)