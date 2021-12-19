package com.midina.stat_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.midina.core_ui.ui.BaseFragment
import com.midina.stat_ui.databinding.FragmentLeagueStatBinding


class LeagueStatFragment : BaseFragment() {

    override val layoutId: Int =  R.layout.fragment_league_stat

    private lateinit var binding: FragmentLeagueStatBinding

    private val viewModel: LeagueStatViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LeagueStatViewModel::class.java]
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

        return binding.root
    }
}