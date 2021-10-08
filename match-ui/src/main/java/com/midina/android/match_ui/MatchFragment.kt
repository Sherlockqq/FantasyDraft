package com.midina.android.match_ui


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
import javax.inject.Inject


class MatchFragment : BaseFragment() {

    private lateinit var binding: FragmentMatchBinding
    val viewModel: MatchViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory )[MatchViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_match,
            container,
            false)

        val bundle = this.arguments
        if (bundle != null) {
            viewModel.homeTeam = bundle.getString("HomeTeam").toString()
            viewModel.guestTeam = bundle.getString("GuestTeam").toString()
            viewModel.score = bundle.getString("Score").toString()
            viewModel.date = bundle.getString("Date").toString()
            setView()
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel



        viewModel.events.observe(viewLifecycleOwner, {handleEvents(it)})

        return binding.root
    }

    private fun handleEvents(event: UiEvent){
        when (event){
            is UiEvent.Success -> onSuccess(event.weather)
            is UiEvent.EmptyState -> onEmptyState()
            is UiEvent.Error -> onError()
        }
    }

    private fun onSuccess(weather : MatchWeather){
        Log.d("MatchFragment","Retrofit Success")
        binding.tvWeather.text=weather.weather
    }

    private fun onEmptyState(){
        Log.d("MatchFragment","Retrofit EmptyState")
        //TODO Something
    }
    private fun onError(){
        Log.d("MatchFragment","Retrofit Error")
        //TODO Something
    }



    private fun setView(){
        binding.tvHomeTeam.text = viewModel.homeTeam
        binding.tvGuestTeam.text = viewModel.guestTeam
        Glide.with(this).load(getImage(viewModel.homeTeam)).into(binding.ivHomeTeam)
        Glide.with(this).load(getImage(viewModel.guestTeam)).into(binding.ivGuestTeam)
        Glide.with(this).load(getStadium(viewModel.homeTeam)).into(binding.ivStadium)

    }

    private fun getImage(team : String): Int{
        when(team){
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

    private fun getStadium(team : String): Int{
        when(team){
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