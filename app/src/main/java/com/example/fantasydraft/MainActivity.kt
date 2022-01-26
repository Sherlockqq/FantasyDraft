package com.example.fantasydraft

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fantasydraft.databinding.ActivityMainBinding
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.midina.core_ui.ui.OnBottomNavItemSelectListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.midina.core_ui.ui.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val LAST_FRAGMENT = "LAST_FRAGMENT"
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    OnBottomNavHideListener, OnBottomNavItemSelectListener {

    private lateinit var dataStore: DataStore<Preferences>

    private lateinit var binding: ActivityMainBinding

    private val fragments: ArrayList<Int> = arrayListOf(
        R.id.fixturesFragment,
        R.id.draftFragment,
        R.id.matchFragment,
        R.id.registrationFragment,
        R.id.loginFragment,
        R.id.clubFragment
    )

    override fun onResume() {
        super.onResume()
        onShowBottomNavView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: ")
        binding = ActivityMainBinding.inflate(layoutInflater)

        setTheme(getTeamTheme())

        setContentView(binding.root)

        dataStore = createDataStore("last_fragment")

        val navView: BottomNavigationView = binding.bottomNavigation.apply {
            itemIconTintList = null
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fixturesFragment, R.id.draftFragment, R.id.clubFragment
            )
        )

        lifecycleScope.launch {
            navToLastFragmentDataStore(navController)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val fragmentId = fragments.indexOf(destination.id)
            lifecycleScope.launch {
                saveToLastFragmentDataStore(fragmentId)
            }
        }

        openBundle()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onHideBottomNavView() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        binding.bottomNavigation.startAnimation(anim)
        binding.bottomNavigation.isVisible = false
    }

    override fun onShowBottomNavView() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        binding.bottomNavigation.startAnimation(anim)
        binding.bottomNavigation.isVisible = true
    }

    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val team = extras.getString("Team")
            if (!team.isNullOrEmpty()) {
                val menu: Menu = binding.bottomNavigation.menu
                val item = menu.findItem(R.id.club_navigation)
                item.setIcon(getImage(team))
            }
        }
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

    private fun getTeamTheme(): Int {
        val sPref =  this.getSharedPreferences(
            "SplashActivity",
            AppCompatActivity.MODE_PRIVATE
        )
        val team = sPref?.getString(BaseFragment.SAVED_TEAM, "")
        if (team != null) {
            when (team) {
                "Динамо Киев" -> return R.style.Theme_Dynamo
            }
        }
        return R.style.Theme_FantasyDraft
    }

    override fun highlightItem(itemId: Int) {
        val menu: Menu = binding.bottomNavigation.menu
        when (itemId) {
            R.id.fixtures_navigation -> {
                val item = menu.findItem(R.id.fixtures_navigation)
                //TODO setIcon
            }
            R.id.draft_navigation -> {
                val item = menu.findItem(R.id.draft_navigation)
            }
            R.id.club_navigation -> {
                val item = menu.findItem(R.id.club_navigation)
            }
        }
    }

    private suspend fun navToLastFragmentDataStore(navController: NavController) {
        val dataStoreKey = preferencesKey<Int>(LAST_FRAGMENT)
        val preferences = dataStore.data.first()
        val result = preferences[dataStoreKey]

        if (result != null) {
            if (result >= 0 && result < fragments.size) {
                // Navigate to this fragment
                when (result) {
                    1 -> navController.navigate(R.id.action_draft_navigation)
                    2 -> navController.navigate(R.id.action_match_navigation)
                    3 -> {
                        navController.navigate(R.id.action_draft_navigation)
                        navController.navigate(R.id.action_registration_navigation)
                    }
                    4 -> {
                        navController.navigate(R.id.action_draft_navigation)
                        navController.navigate(R.id.action_login_navigation)
                    }
                    5 -> navController.navigate(R.id.action_club_navigation)
                }
            }
        }
    }

    private suspend fun saveToLastFragmentDataStore(fragmentId: Int) {
        val dataStoreKey = preferencesKey<Int>(LAST_FRAGMENT)
        dataStore.edit { fragments ->
            fragments[dataStoreKey] = fragmentId
        }
    }

}