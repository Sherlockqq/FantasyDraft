package com.midina.stat_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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

        lifecycleScope.launchWhenCreated {
            viewModel.topAssistantEvents
                .collect {
                    handleTopAssistantUiEvent(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.topCleanSheetsEvents
                .collect {
                    handleTopCleanSheetsUiEvent(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.topTeamGoalsEvents
                .collect {
                    handleTopTeamGoalsResultUiEvent(it)
                }
        }

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.btSeasonStat.button.setText(R.string.assists)

        return binding.root
    }

    private fun handleSeasonUiEvent(event: SeasonResultUiEvent) {
        when (event) {
            is SeasonResultUiEvent.Success -> {
                binding.tvSeason.text = getString(R.string.season_statistics, event.season, event.season + 1)
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
                binding.scorer.tvResult.text = getString(R.string.goals, event.player.goals)

                Glide
                    .with(this)
                    .load(event.player.photoURL)
                    .into(binding.scorer.ivResult)

                binding.scorer.root.isVisible = true
            }
            is TopScorerResultUiEvent.EmptyState -> {
                Log.d(TAG, "TopScorer EmptyState")
            }
            is TopScorerResultUiEvent.Error -> {
                Log.d(TAG, "TopScorer Error")
            }
        }
    }


    private fun handleTopAssistantUiEvent(event: TopAssistantResultUiEvent) {
        when (event) {
            is TopAssistantResultUiEvent.Success -> {
                binding.assistant.tvResult.text = getString(R.string.assists, event.player.assists)

                Glide
                    .with(this)
                    .load(event.player.photoURL)
                    .into(binding.assistant.ivResult)

                binding.assistant.root.isVisible = true
            }
            is TopAssistantResultUiEvent.EmptyState -> {
                Log.d(TAG, "TopScorer EmptyState")
            }
            is TopAssistantResultUiEvent.Error -> {
                Log.d(TAG, "TopScorer Error")
            }
        }
    }

    private fun handleTopCleanSheetsUiEvent(event: TopCleanSheetResultUiEvent) {
        when (event) {
            is TopCleanSheetResultUiEvent.Success -> {
                binding.cleanSheets.tvResult.text = getString(
                    R.string.clean_sheets,
                    event.team.cleanSheets
                )

                Glide
                    .with(this)
                    .load(event.team.photoURL)
                    .into(binding.cleanSheets.ivResult)

                binding.cleanSheets.root.isVisible = true
            }
            is TopCleanSheetResultUiEvent.EmptyState -> {
                Log.d(TAG, "TopScorer EmptyState")
            }
            is TopCleanSheetResultUiEvent.Error -> {
                Log.d(TAG, "TopScorer Error")
            }
        }
    }

    private fun handleTopTeamGoalsResultUiEvent(event: TopTeamGoalsResultUiEvent) {
        when (event) {
            is TopTeamGoalsResultUiEvent.Success -> {
                binding.teamGoals.tvResult.text = getString(R.string.goals, event.team.goals)

                Glide
                    .with(this)
                    .load(event.team.photoURL)
                    .into(binding.teamGoals.ivResult)

                binding.teamGoals.root.isVisible = true
            }
            is TopTeamGoalsResultUiEvent.EmptyState -> {
                Log.d(TAG, "TopScorer EmptyState")
            }
            is TopTeamGoalsResultUiEvent.Error -> {
                Log.d(TAG, "TopScorer Error")
            }
        }
    }

}