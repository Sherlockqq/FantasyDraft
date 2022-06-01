package com.midina.match_ui

import android.annotation.SuppressLint
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
import com.midina.android.match_domain.model.Match
import com.midina.core_ui.ui.BaseFragment
import com.midina.match_ui.databinding.FragmentMatchBinding
import kotlinx.coroutines.flow.collect

private const val DAYS_INDEX = 2
private const val HOURS_INDEX = 1
private const val MINUTES_INDEX = 0
private const val TAG = "MatchFragment"

class MatchFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_match
    private lateinit var binding: FragmentMatchBinding

    val viewModel: MatchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MatchViewModel::class.java]
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
            viewModel.matchEvents
                .collect {
                    handleMatchEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.weatherEvents
                .collect {
                    handleWeatherEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.scoreOrDateEvents
                .collect {
                    handleScoreOrDateEvents(it)
                }
        }

        return binding.root
    }

    private fun handleMatchEvents(event: UiMatchEvent) {
        when (event) {
            is UiMatchEvent.Success -> onSuccess(event.match)
            is UiMatchEvent.Loading -> onLoading()
            is UiMatchEvent.Error -> onError()
        }
    }

    private fun handleWeatherEvents(event: UiWeatherEvent) {
        when (event) {
            is UiWeatherEvent.Success -> {
                Glide
                    .with(this)
                    .load(getWeatherImage(event.weather.weather))
                    .into(binding.ivWeather)
                binding.tvTemperature.text =
                    getString(R.string.temperature, event.weather.temperature)
            }
            is UiWeatherEvent.EmptyState -> {

            }
            is UiWeatherEvent.Error -> {

            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun handleScoreOrDateEvents(event: UiScoreOrDateEvent) {
        when (event) {
            is UiScoreOrDateEvent.HasScore -> {
                binding.tvScore.text = viewModel.score.value
                binding.tvScore.setBackgroundColor(R.color.design_default_color_primary)
            }
            is UiScoreOrDateEvent.HasDate -> {
                Log.d("MatchFragment", "Check dateArr ; ${viewModel.dateArr.value}")
                binding.tvScore.text = getString(R.string.timer, viewModel.dateArr.value[DAYS_INDEX],
                    viewModel.dateArr.value[HOURS_INDEX],
                    viewModel.dateArr.value[MINUTES_INDEX]
                )
            }
            UiScoreOrDateEvent.Default -> {
                Log.d(TAG, "UiScoreOrDateEvent.Default")
            }
        }
    }

    private fun onSuccess(match: Match) {
        Log.d("MatchFragment", "Retrofit Success")
        binding.tvHomeTeam.text = match.teams.home.name
        binding.tvAwayTeam.text = match.teams.away.name
        binding.tvDate.text = match.fixture.date
        binding.tvScore.text = getString(R.string.score, match.goals.home, match.goals.away)
        Glide.with(this).load(match.fixture.venue.stadium).into(binding.ivStadium)
        Glide.with(this).load(match.teams.home.logo).into(binding.ivHomeTeam)
        Glide.with(this).load(match.teams.away.logo).into(binding.ivAwayTeam)
    }

    private fun onLoading() {
        Log.d("MatchFragment", "Retrofit Error")
    }

    private fun onError() {
        Log.d("MatchFragment", "Retrofit Error")
        //TODO Something
    }

    private fun getWeatherImage(weather: String): Int {
        when (weather) {
            "Clear" -> return R.drawable.weather_clear
            "Clouds" -> return R.drawable.weather_clouds
            "Rain" -> return R.drawable.weather_rain
        }
        return R.drawable.connection_error
    }
}