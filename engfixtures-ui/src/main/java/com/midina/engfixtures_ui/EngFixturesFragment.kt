package com.midina.engfixtures_ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.midina.core_ui.ui.BaseFragment
import com.midina.engfixtures_ui.databinding.FragmentEngFixturesBinding
import kotlinx.coroutines.flow.collect


class EngFixturesFragment : BaseFragment() {

    private lateinit var binding: FragmentEngFixturesBinding
    private lateinit var adapter: TourPageAdapter
    private val TAG = "EngFixturesFragment"

    override val layoutId: Int = R.layout.fragment_eng_fixtures


    val viewModel: EngFixturesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EngFixturesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.events
                .collect {
                    handleEvents(it)
                }
        }

        return binding.root
    }

    private fun handleEvents(event: UiEvent) {
        when (event) {
            is UiEvent.Success -> {
                adapter = activity?.let { TourPageAdapter(it, event.matches) }!!
                Log.d(TAG," matches : ${event.matches}")
                binding.pager.adapter = adapter
            }
            is UiEvent.Error -> {}
        }
    }

}