package com.mirkamalg.presentation.utils.recycler_view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Mirkamal Gasimov on 05.10.2021.
 */

class GenericDiffCallback<D> : DiffUtil.ItemCallback<D>() {

    override fun areItemsTheSame(oldItem: D, newItem: D): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: D, newItem: D): Boolean {
        return newItem == oldItem
    }
}