package com.mirkamalg.data.utils

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class YoutubeApiKeyInterceptor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(
            request.newBuilder()
                .url(
                    request.url.newBuilder()
                        .addQueryParameter("key", apiKey)
                        .build()
                )
                .build()
        )
    }
}