package com.mirkamalg.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mirkamalg.presentation.viewmodel.BaseViewModel
import timber.log.Timber

/**
 * Created by Mirkamal Gasimov on 12.02.2022.
 */

abstract class BaseFragment<B : ViewBinding, S, E, VM : BaseViewModel<S, E>> : Fragment() {

    protected var binding: B? = null
    protected abstract val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> B
    abstract val onBind: B.() -> Unit

    abstract val viewModel: VM

    /**
     * Override this to do observable (like [LiveData]) observations on receiver [VM]
     */
    abstract fun VM.onObserve()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = onInflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("Current fragment: ${this::class.simpleName}")
        binding?.onBind()
        viewModel.onObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}