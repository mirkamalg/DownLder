package com.mirkamalg.domain.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

abstract class FileDownloadCompletedReceiver : BroadcastReceiver() {

    abstract fun onFileDownloadCompleted(context: Context?, intent: Intent?)

}