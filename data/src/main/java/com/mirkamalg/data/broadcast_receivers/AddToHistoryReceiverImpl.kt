package com.mirkamalg.data.broadcast_receivers

import android.content.Context
import android.content.Intent
import com.mirkamalg.domain.broadcast_receivers.AddToHistoryReceiver
import com.mirkamalg.domain.broadcast_receivers.FileDownloadCompletedReceiver
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import com.mirkamalg.domain.utils.SingleLiveEvent

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

class AddToHistoryReceiverImpl(
    private val addToHistoryTrigger: SingleLiveEvent<ConversionUseCase.InsertNewDownloadUseCase.InsertNewDownloadParams>
) : AddToHistoryReceiver() {

    override fun onAddToHistory(
        entity: ConversionUseCase.InsertNewDownloadUseCase.InsertNewDownloadParams
    ) {
        addToHistoryTrigger.postValue(entity)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getParcelableExtra<ConversionUseCase.InsertNewDownloadUseCase.InsertNewDownloadParams?>(
            FileDownloadCompletedReceiver.EXTRA_DOWNLOADED_FILE
        )
            ?.let {
                onAddToHistory(it)
            }
    }
}