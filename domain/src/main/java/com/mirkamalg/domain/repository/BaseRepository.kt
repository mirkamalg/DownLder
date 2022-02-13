package com.mirkamalg.domain.repository

import com.mirkamalg.domain.exceptions.BadRequestException
import com.mirkamalg.domain.exceptions.ServerException
import com.mirkamalg.domain.exceptions.UnauthorizedException
import com.mirkamalg.domain.exceptions.UnknownException
import retrofit2.Response

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

interface BaseRepository {

    fun <T> handleResponse(response: Response<T>) = when {
        response.isSuccessful -> response.body()!!
        else -> throw when (response.code()) {
            400 -> BadRequestException
            401 -> UnauthorizedException
            in 500..599 -> ServerException
            else -> UnknownException
        }
    }

}