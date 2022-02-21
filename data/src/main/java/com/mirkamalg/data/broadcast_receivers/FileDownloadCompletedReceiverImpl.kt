package com.mirkamalg.data.broadcast_receivers

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mirkamalg.domain.broadcast_receivers.FileDownloadCompletedReceiver
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.CoroutineContext


/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

class FileDownloadCompletedReceiverImpl(
    private val coroutineContext: CoroutineContext,
    private val downloadId: Long,
    private val intendedFile: File,
    private val intendedVideo: VideoMetaDataEntity,
    private val type: String,
    private val context: Context
) : FileDownloadCompletedReceiver() {

    override fun onFileDownloadCompleted(context: Context?, intent: Intent?) {
        Timber.d("File download completed! => ${intendedFile.name}")
        CoroutineScope(coroutineContext).launch {
            copyFileToSharedStorage(intendedFile)
        }
    }

    @Suppress("DEPRECATION")
    private fun copyFileToSharedStorage(file: File) {
        try {
            Timber.d("Started copying file")
            val cr = context.contentResolver
            var values = ContentValues()
            val displayName = file.name
            if (Build.VERSION.SDK_INT >= 29) {
                // RELATIVE_PATH is limited, e.g., for MediaStore.Downloads the only option is DIRECTORY_DOWNLOADS
                values.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
                values.put(MediaStore.MediaColumns.IS_PENDING, true)
            } else {
                // on lower system versions the folder must exists, but you can use old-school method instead ("new File")
                values.put(
                    MediaStore.MediaColumns.DATA,
                    Environment.getExternalStorageDirectory()
                        .toString() + "/" + Environment.DIRECTORY_DOWNLOADS + displayName
                )
            }
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            val target: Uri = if (Build.VERSION.SDK_INT >= 29) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Files.getContentUri("external")
            }
            val uri: Uri = cr.insert(target, values) ?: return
            val inputStream: InputStream = FileInputStream(file)
            val outputStream: OutputStream? = cr.openOutputStream(uri, "rw")
            val bytes = ByteArray(8192)
            var r: Int
            while (inputStream.read(bytes).also { r = it } != -1) {
                outputStream?.write(bytes, 0, r)
            }
            outputStream?.flush()
            outputStream?.close()
            inputStream.close()
            if (Build.VERSION.SDK_INT >= 29) {
                values = ContentValues()
                values.put(MediaStore.Images.ImageColumns.IS_PENDING, false)
                cr.update(uri, values, null, null)
            }

            sendDownloadCompletedBroadcast()
        } catch (e: Exception) {
            Timber.e("Failed to copy!")
            Timber.e(e)
        } finally {
            Timber.d("Deleting file (${file.absolutePath}) and unregistering receiver ($this)")
            file.delete()
            this.context.unregisterReceiver(this)
        }
    }

    private fun sendDownloadCompletedBroadcast() {
        intendedVideo.apply {
            LocalBroadcastManager.getInstance(context).sendBroadcast(
                Intent(ACTION_ADD_TO_HISTORY).apply {
                    putExtra(
                        EXTRA_DOWNLOADED_FILE,
                        ConversionUseCase.InsertNewDownloadUseCase.InsertNewDownloadParams(
                            title,
                            description,
                            channel,
                            length,
                            thumbnailUrl,
                            viewCount,
                            likeCount,
                            videoId,
                            this@FileDownloadCompletedReceiverImpl.type
                        )
                    )
                }
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
            onFileDownloadCompleted(context, intent)
        }
    }
}