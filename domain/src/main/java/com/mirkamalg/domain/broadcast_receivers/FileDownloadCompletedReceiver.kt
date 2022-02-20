package com.mirkamalg.domain.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

abstract class FileDownloadCompletedReceiver : BroadcastReceiver() {

    abstract fun onFileDownloadCompleted(context: Context?, intent: Intent?)

    companion object {
        const val ACTION_ADD_TO_HISTORY = "ACTION_ADD_TO_HISTORY"
        const val EXTRA_DOWNLOADED_FILE = "EXTRA_DOWNLOADED_FILE"
    }

}