package com.mirkamalg.presentation.utils.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

/**
 * Created by Mirkamal Gasimov on 05.10.2021.
 */

class RecyclerListAdapter<VB : ViewBinding, D>(
    private val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val onBind: (VB, D) -> Unit
) : ListAdapter<D, RecyclerViewHolder<VB, D>>(GenericDiffCallback<D>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<VB, D> {
        return RecyclerViewHolder.create(parent, onInflate, onBind)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder<VB, D>, position: Int) {
        holder.bind(getItem(position))
    }

}