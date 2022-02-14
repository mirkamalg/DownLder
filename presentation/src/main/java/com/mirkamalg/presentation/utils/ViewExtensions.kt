package com.mirkamalg.presentation.utils

import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Mirkamal Gasimov on 15.02.2022.
 */

var TextInputLayout.text: String?
    get() = editText?.text?.toString()
    set(value) {
        editText?.apply {
            setText(value)
            setSelection(length())
        }
    }