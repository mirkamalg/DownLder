package com.mirkamalg.downlder

import android.app.Application
import android.content.pm.PackageManager
import com.mirkamalg.downlder.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Mirkamal Gasimov on 6.02.2022.
 */

class DownLderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement) =
                "${super.createStackElementTag(element)}:${element.lineNumber}"
        })

        startKoin {
            androidContext(this@DownLderApp)
            properties(
                mapOf(
                    "youtubeApiKey" to applicationContext.packageManager
                        .getApplicationInfo(
                            applicationContext.packageName,
                            PackageManager.GET_META_DATA
                        ).metaData["youtubeApiKey"].toString()
                )
            )
            modules(appModules)
        }
    }
}