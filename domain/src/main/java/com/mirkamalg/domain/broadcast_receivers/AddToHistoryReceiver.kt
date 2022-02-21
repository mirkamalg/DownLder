package com.mirkamalg.domain.broadcast_receivers

import android.content.BroadcastReceiver
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase

/**
 * Created by Mirkamal Gasimov on 20.02.2022.
 */

abstract class AddToHistoryReceiver : BroadcastReceiver() {

    abstract fun onAddToHistory(entity: ConversionUseCase.InsertNewDownloadUseCase.InsertNewDownloadParams)

}