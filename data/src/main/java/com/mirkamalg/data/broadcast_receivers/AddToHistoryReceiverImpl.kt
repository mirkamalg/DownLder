package com.mirkamalg.data.broadcast_receivers

import android.content.Context
import android.content.Intent
import com.mirkamalg.domain.broadcast_receivers.AddToHistoryReceiver
import com.mirkamalg.domain.broadcast_receivers.FileDownloadCompletedReceiver
import com.mirkamalg.domain.model.conversion.VideoMetaDataEntity
import com.mirkamalg.domain.utils.SingleLiveEvent

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

class AddToHistoryReceiverImpl(
    private val addToHistoryTrigger: SingleLiveEvent<VideoMetaDataEntity>
) : AddToHistoryReceiver() {

    override fun onAddToHistory(
        entity: VideoMetaDataEntity
    ) {
        addToHistoryTrigger.postValue(entity)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getParcelableExtra<VideoMetaDataEntity?>(FileDownloadCompletedReceiver.EXTRA_DOWNLOADED_FILE)
            ?.let {
                onAddToHistory(it)
            }
    }
}