package com.midina.team_ui

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavItemSelectListener
import com.midina.team_ui.databinding.FragmentClubBinding

class ClubFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_club

    private lateinit var binding: FragmentClubBinding
    private var listener: OnBottomNavItemSelectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val contextThemeWrapper: Context = ContextThemeWrapper(activity, getTeamTheme())

        val localInflater = inflater.cloneInContext(contextThemeWrapper)

        binding = DataBindingUtil.inflate(
            localInflater,
            layoutId,
            container,
            false
        )
        setView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        highlightIcon()
    }

    private fun highlightIcon() {
        if (context is OnBottomNavItemSelectListener) {
            listener = context as OnBottomNavItemSelectListener
            listener?.highlightItem(R.id.club_navigation)
        }
    }

    private fun setView() {
        val sPref =  this.activity?.getSharedPreferences(
            "SplashActivity",
            AppCompatActivity.MODE_PRIVATE
        )
        val team = sPref?.getString(SAVED_TEAM, "")
        if (team != null) {
            binding.vTeamView.ivStadium.setImageResource(getStadium(team))
            binding.vTeamView.ivTeamLogo.setImageResource(getLogo(team))
            binding.vTeamView.tvTeamName.text = team
            binding.vTeamView.tvStadiumName.text = "Олимпийский"
        }
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

    private fun getLogo(team: String): Int {
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
}