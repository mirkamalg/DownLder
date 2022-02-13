package com.mirkamalg.data.utils

import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

class DocumentCall(private val delegate: Call<Document>) : Call<Document> by delegate {
    override fun enqueue(callback: Callback<Document>) {
        delegate.enqueue(object : Callback<Document> {
            override fun onResponse(call: Call<Document>, response: Response<Document>) {
                callback.onResponse(this@DocumentCall, response)
            }

            override fun onFailure(call: Call<Document>, t: Throwable) = throw t

        })
    }
}