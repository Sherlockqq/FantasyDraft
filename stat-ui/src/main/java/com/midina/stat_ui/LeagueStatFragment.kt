package com.midina.stat_ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.midina.core_ui.ui.BaseFragment
import com.midina.stat_ui.databinding.FragmentLeagueStatBinding
import kotlinx.coroutines.flow.collect


class LeagueStatFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_league_stat

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

        lifecycleScope.launchWhenCreated {
            viewModel.seasonEvents
                .collect {
                    handleSeasonUiEvent(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.topScorerEvents
                .collect {
                    handleTopScorerUiEvent(it)
                }
        }

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    private fun handleSeasonUiEvent(event: SeasonResultUiEvent) {
        when (event) {
            is SeasonResultUiEvent.Success -> {
                binding.tvSeason.text = "${event.season} - ${event.season+1} Top Statistics"
            }
            is SeasonResultUiEvent.EmptyState -> {
                Log.d(TAG, "Season EmptyState")
            }
            is SeasonResultUiEvent.Error -> {
                Log.d(TAG, "Season Error")
            }
        }
    }

    private fun handleTopScorerUiEvent(event: TopScorerResultUiEvent) {
        when (event) {
            is TopScorerResultUiEvent.Success -> {
                binding.tvPlayerGoals.text = "${event.player.goals} Goals"

                Glide
                    .with(this)
                    .load(event.player.photoURL)
                    .into(binding.ivTopScorer)
            }
            is TopScorerResultUiEvent.EmptyState -> {
                Log.d(TAG, "TopScorer EmptyState")
            }
            is TopScorerResultUiEvent.Error -> {
                Log.d(TAG, "TopScorer Error")
            }
        }
    }


}