package com.midina.android.match_ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_ui.databinding.FragmentMatchBinding
import com.midina.core_ui.ui.BaseFragment
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject
import javax.inject.Named

private const val DAYS_INDEX = 2
private const val HOURS_INDEX = 1
private const val MINUTES_INDEX = 0

class MatchFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_match
    private lateinit var binding: FragmentMatchBinding

    val viewModel: MatchViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory)[MatchViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        val bundle = this.arguments
        if (bundle != null) {
            setView()
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.events.observe(viewLifecycleOwner, { handleEvents(it) })
        viewModel.scoreOrDateEvents.observe(viewLifecycleOwner, { handleScoreOrDateEvents(it) })

        return binding.root
    }

    private fun handleEvents(event: UiEvent) {
        when (event) {
            is UiEvent.Success -> onSuccess(event.weather)
            is UiEvent.EmptyState -> onEmptyState()
            is UiEvent.Error -> onError()
        }
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun handleScoreOrDateEvents(event: UiScoreOrDateEvent) {
        when (event) {
            is UiScoreOrDateEvent.HasScore -> {
                binding.tvScore.text = viewModel.score
                binding.tvScore.setBackgroundColor(R.color.design_default_color_primary)
            }
            is UiScoreOrDateEvent.HasDate -> {
                binding.tvScore.text = "Days: ${viewModel.dateArr.value?.get(DAYS_INDEX)} " +
                        "Hours: ${viewModel.dateArr.value?.get(HOURS_INDEX)} " +
                        "Minutes: ${viewModel.dateArr.value?.get(MINUTES_INDEX)}"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(weather: MatchWeather) {
        Log.d("MatchFragment", "Retrofit Success")
        Glide.with(this).load(getWeatherImage(weather.weather)).into(binding.ivWeather)
        binding.tvTemperature.text = weather.temperature.toString() + "°C"
    }

    private fun onEmptyState() {
        Log.d("MatchFragment", "Retrofit EmptyState")
        //TODO Something
    }

    private fun onError() {
        Log.d("MatchFragment", "Retrofit Error")
        //TODO Something
    }

    private fun setView() {
        binding.tvHomeTeam.text = viewModel.getHomeTeamName()
        binding.tvGuestTeam.text = viewModel.getGuestTeamName()
        binding.tvDate.text = viewModel.date
        Glide.with(this).load(getImage(viewModel.homeTeam)).into(binding.ivHomeTeam)
        Glide.with(this).load(getImage(viewModel.guestTeam)).into(binding.ivGuestTeam)
        Glide.with(this).load(getStadium(viewModel.homeTeam)).into(binding.ivStadium)
    }

    private fun getWeatherImage(weather: String): Int {
        when (weather) {
            "Clear" -> return R.drawable.weather_clear
            "Clouds" -> return R.drawable.weather_clouds
            "Rain" -> return R.drawable.weather_rain
        }
        return R.drawable.connection_error
    }

    private fun getImage(team: String): Int {
        when (team) {
            "Львов" -> return R.drawable.lviv_logo
            "Верес" -> return R.drawable.veres_logo
            "Шахтер Донецк" -> return R.drawable.shakhtar_logo
            "Металлист 1925" -> return R.drawable.metallist25_logo
            "Десна" -> return R.drawable.desna_logo
            "Заря" -> return R.drawable.zarya_logo
            "Ворскла" -> return R.drawable.vorskla_logo
            "Динамо Киев" -> return R.drawable.dynamo_logo
            "Мариуполь" -> return R.drawable.mariupol_logo
            "Колос К" -> return R.drawable.kolos_logo
            "Ингулец" -> return R.drawable.ingulets_logo
            "Рух Львов" -> return R.drawable.rukh_logo
            "Черноморец" -> return R.drawable.chornomorets_logo
            "Александрия" -> return R.drawable.oleksandriya_logo
            "Днепр-1" -> return R.drawable.dnipro1_logo
            "Минай" -> return R.drawable.minaj_logo
        }
        return R.drawable.connection_error
    }

    private fun getStadium(team: String): Int {
        when (team) {
            "Львов" -> return R.drawable.stadium_arena_lviv
            "Верес" -> return R.drawable.stadium_veres
            "Шахтер Донецк" -> return R.drawable.stadium_olimpiyskii
            "Металлист 1925" -> return R.drawable.stadium_metallist25
            "Десна" -> return R.drawable.stadium_desna
            "Заря" -> return R.drawable.stadium_zarya
            "Ворскла" -> return R.drawable.stadium_vorskla
            "Динамо Киев" -> return R.drawable.stadium_olimpiyskii
            "Мариуполь" -> return R.drawable.stadium_mariopol
            "Колос К" -> return R.drawable.stadium_kolos
            "Ингулец" -> return R.drawable.stadium_ingulets
            "Рух Львов" -> return R.drawable.stadium_arena_lviv
            "Черноморец" -> return R.drawable.stadium_chernomorets
            "Александрия" -> return R.drawable.stadium_oleksandriya
            "Днепр-1" -> return R.drawable.stadium_dnipro1
            "Минай" -> return R.drawable.stadium_minaj
        }
        return R.drawable.connection_error
    }
}


// Clear, clouds, rain