package com.mirkamalg.presentation.utils.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Mirkamal Gasimov on 05.10.2021.
 */

class RecyclerViewHolder<VB : ViewBinding, D>(
    private val binding: VB,
    private val onBind: (VB, D) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: D) {
        onBind(binding, data)
    }

    companion object {
        fun <VB : ViewBinding, D> create(
            viewGroup: ViewGroup,
            onInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
            onBind: (VB, D) -> Unit
        ): RecyclerViewHolder<VB, D> {
            return RecyclerViewHolder(
                onInflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                onBind
            )
        }
    }
}