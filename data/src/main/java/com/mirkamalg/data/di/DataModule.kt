package com.mirkamalg.data.di

import com.mirkamalg.data.BuildConfig
import com.mirkamalg.data.dataSource.remote.downloadHtmlPage.DownloadContentRemoteDataSource
import com.mirkamalg.data.dataSource.remote.videoMetaData.VideoMetaDataRemoteDataSource
import com.mirkamalg.data.repository.DownloadContentRepositoryImpl
import com.mirkamalg.data.repository.VideoMetaDataRepositoryImpl
import com.mirkamalg.data.utils.DocumentCall
import com.mirkamalg.data.utils.FileDownloadCompletedReceiverImpl
import com.mirkamalg.data.utils.YoutubeApiKeyInterceptor
import com.mirkamalg.domain.broadcast_receivers.FileDownloadCompletedReceiver
import com.mirkamalg.domain.di.IO_DISPATCHER
import com.mirkamalg.domain.repository.DownloadContentRepository
import com.mirkamalg.domain.repository.VideoMetaDataRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Created by Mirkamal Gasimov on 6.02.2022.
 */

const val BASE_URL_DOWNLOADER = "https://api.vevioz.com/@api/"
const val BASE_URL_YOUTUBE_DATA = "https://www.googleapis.com/youtube/v3/"

const val HTML_CONVERTER_FACTORY = "HTML_CONVERTER_FACTORY"
const val HTML_CALL_ADAPTER_FACTORY = "HTML_CALL_ADAPTER_FACTORY"
const val RETROFIT_DOWNLOADER = "RETROFIT_DOWNLOADER"
const val RETROFIT_YOUTUBE_DATA = "RETROFIT_YOUTUBE_DATA"
const val OKHTTP_CLIENT_DOWNLOADER = "OKHTTP_CLIENT_DOWNLOADER"
const val OKHTTP_CLIENT_YOUTUBE_DATA = "OKHTTP_CLIENT_YOUTUBE_DATA"

val dataModule = module {

    single(named(RETROFIT_DOWNLOADER)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL_DOWNLOADER)
            .addConverterFactory(get(named(HTML_CONVERTER_FACTORY)))
            .addCallAdapterFactory(get(named(HTML_CALL_ADAPTER_FACTORY)))
            .client(get(named(OKHTTP_CLIENT_DOWNLOADER)))
            .build()
    }

    single(named(RETROFIT_YOUTUBE_DATA)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL_YOUTUBE_DATA)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get(named(OKHTTP_CLIENT_YOUTUBE_DATA)))
            .build()
    }

    single {
        Moshi.Builder().build()
    }

    single(named(OKHTTP_CLIENT_DOWNLOADER)) {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        builder.apply {
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
        }

        builder.build()
    }

    single(named(OKHTTP_CLIENT_YOUTUBE_DATA)) {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        builder.addInterceptor(get<YoutubeApiKeyInterceptor>())

        builder.apply {
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
        }

        builder.build()
    }

    single {
        YoutubeApiKeyInterceptor(getProperty("youtubeApiKey"))
    }

    single<Converter.Factory>(named(HTML_CONVERTER_FACTORY)) {
        object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *>? {
                if (type != Document::class.java) return null
                return Converter<ResponseBody, Document> {
                    Jsoup.parse(it.string())
                }
            }
        }
    }

    single<CallAdapter.Factory>(named(HTML_CALL_ADAPTER_FACTORY)) {
        object : CallAdapter.Factory() {
            override fun get(
                returnType: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): CallAdapter<*, *>? {
                if (returnType != Document::class.java) return null
                return object : CallAdapter<Document, DocumentCall> {
                    override fun responseType(): Type = Document::class.java
                    override fun adapt(call: Call<Document>) = DocumentCall(call)
                }
            }

        }
    }

    single {
        get<Retrofit>(named(RETROFIT_DOWNLOADER)).create(DownloadContentRemoteDataSource::class.java)
    }

    single {
        get<Retrofit>(named(RETROFIT_YOUTUBE_DATA)).create(VideoMetaDataRemoteDataSource::class.java)
    }

    factory<DownloadContentRepository> {
        DownloadContentRepositoryImpl(get())
    }

    factory<VideoMetaDataRepository> {
        VideoMetaDataRepositoryImpl(get())
    }

    factory<FileDownloadCompletedReceiver> { params ->
        FileDownloadCompletedReceiverImpl(
            coroutineContext = get(named(IO_DISPATCHER)),
            downloadId = params.get(),
            intendedFile = params.get(),
            context = androidContext()
        )
    }

}