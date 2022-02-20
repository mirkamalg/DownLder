package com.mirkamalg.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mirkamalg.domain.usecase.conversion.ConversionUseCase
import com.mirkamalg.domain.utils.applyToAll
import com.mirkamalg.presentation.R
import com.mirkamalg.presentation.databinding.FragmentConversionBinding
import com.mirkamalg.presentation.utils.text
import com.mirkamalg.presentation.viewmodel.ConversionEffect
import com.mirkamalg.presentation.viewmodel.ConversionState
import com.mirkamalg.presentation.viewmodel.ConversionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Mirkamal Gasimov on 13.02.2022.
 */

class ConversionFragment :
    BaseFragment<FragmentConversionBinding, ConversionState, ConversionEffect, ConversionViewModel>() {

    private var getPermission: ActivityResultLauncher<String>? = null

    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentConversionBinding
        get() = FragmentConversionBinding::inflate
    override val onBind: FragmentConversionBinding.() -> Unit
        get() = {
            setListeners()
        }
    override val viewModel: ConversionViewModel by viewModel()

    override fun ConversionViewModel.onObserve() {
        binding?.apply {
            state.observe(viewLifecycleOwner) {
                syncState(it)
            }
            effect.observe(viewLifecycleOwner) {
                when (it) {
                    is ConversionEffect.Error -> {
                        showSnackBar(it.message)
                    }
                    is ConversionEffect.InvalidUrl -> {
                        textInputLayoutUrl.error = getString(R.string.err_invalid_url)
                    }
                }
            }
        }
    }

    private fun FragmentConversionBinding.setListeners() {
        textInputLayoutUrl.editText?.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val editText = view as EditText
                viewModel.getVideoMetaData(editText.text.toString())
            }
            false
        }
        fabDownload.setOnClickListener {
            viewModel.openDownloadPage(it.context)
        }
    }

    private fun syncState(state: ConversionState?) {
        binding?.apply {
            state?.apply {
                progressBar.isVisible = loading
                applyToAll(imageViewThumbnail, textViewTitle, textViewDescription) {
                    isVisible = videoMetaDataEntity != null
                }
                if (videoMetaDataEntity == null) {
                    fabDownload.hide()
                    fabDownload.setOnClickListener(null)
                } else {
                    fabDownload.show()
                    fabDownload.setOnClickListener { view ->
                        MaterialAlertDialogBuilder(view.context).apply {
                            setTitle(R.string.title_choose_format)
                            setItems(
                                ConversionUseCase.DownloadType.values()
                                    .map {
                                        getString(it.strId)
                                    }.toTypedArray()
                            ) { dialog, which ->
                                dialog.dismiss()
                                viewModel.downloadContent(
                                    ConversionUseCase.DownloadType.values()[which]
                                )
                            }
                            show()
                        }
                    }
                }
                textInputLayoutUrl.text = searchedUrl
                imageViewThumbnail.load(videoMetaDataEntity?.thumbnailUrl)
                textViewTitle.text = videoMetaDataEntity?.title
                textViewDescription.text = videoMetaDataEntity?.description
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }
    }

    override fun onStart() {
        super.onStart()
        context?.let {
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                getPermission?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}