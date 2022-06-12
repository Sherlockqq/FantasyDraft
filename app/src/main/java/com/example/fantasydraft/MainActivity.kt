package com.example.fantasydraft

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.fantasydraft.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import com.midina.core_ui.ui.*
import com.midina.core_ui.ui.BaseFragment.Companion.TEAM_ID
import com.midina.core_ui.ui.BaseFragment.Companion.FAVOURITE_TEAM_LOGO
import com.midina.matches_ui.OnArrowClickListener
import com.midina.matches_ui.fixtures.FixturesFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


private const val LAST_FRAGMENT = "LAST_FRAGMENT"
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    OnBottomNavHideListener, OnBottomNavItemSelectListener, OnArrowClickListener,
    OnActionBarHideListener {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var binding: ActivityMainBinding

    private var listener: OnFragmentUiBlockListener? = null
    private val firebaseDynamicLinks = FirebaseDynamicLinks.getInstance()
    private val fAuth = Firebase.auth

    private val fragments: ArrayList<Int> = arrayListOf(
        R.id.fixturesFragment,
        R.id.draftFragment,
        R.id.matchFragment,
        R.id.registrationFragment,
        R.id.loginFragment,
        R.id.clubFragment,
        R.id.leagueStatFragment
    )

    override fun onResume() {
        super.onResume()
        onShowBottomNavView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO signatures is deprecated. FIX IT!
//        try {
//            val info = packageManager.getPackageInfo(
//                "com.example.fantasydraft",
//                PackageManager.GET_SIGNATURES
//            )
//            info.signatures.forEach { signature ->
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//
//        } catch (e: NoSuchAlgorithmException) {
//
//        }

        Log.d(TAG, "onCreate: ")
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setTheme(R.style.Theme_FantasyDraft)

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
                R.id.fixturesFragment, R.id.draftFragment,
                R.id.leagueStatFragment, R.id.clubFragment
            )
        )

        lifecycleScope.launch {
            navToLastFragmentDataStore(navController)
        }
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val fragmentId = fragments.indexOf(destination.id)
            lifecycleScope.launch {
                saveToLastFragmentDataStore(fragmentId)
            }
        }
        openExtras()

        setContentView(binding.root)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        blockFragmentUi()
        handleDynamicLink()
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

    private fun openExtras() {
        val extras = intent.extras
        if (extras != null) {
            val teamLogo = extras.getString(FAVOURITE_TEAM_LOGO)
            val teamId = extras.getInt(TEAM_ID)
            setSharedPreferencesTeamId(teamId)
            if (!teamLogo.isNullOrEmpty()) {
                val menu: Menu = binding.bottomNavigation.menu
                val item = menu.findItem(R.id.club_navigation)

                Glide.with(this)
                    .asBitmap()
                    .load(teamLogo)
                    .error(R.drawable.connection_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onLoadCleared(placeholder: Drawable?) {}
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            item.icon = BitmapDrawable(resources, resource)
                        }
                    })
            }
        }
    }

    private fun getTeamTheme(): Int {
        val sPref = this.getSharedPreferences(
            "SplashActivity",
            MODE_PRIVATE
        )
        val team = sPref?.getString(BaseFragment.TEAM_ID, "")

        return R.style.Theme_FantasyDraft
    }


    //TODO DELETE THIS. NOT NECCESERY
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
            R.id.stat_navigation -> {
                val item = menu.findItem(R.id.stat_navigation)
            }
        }
    }

    private suspend fun navToLastFragmentDataStore(navController: NavController) {
        try {
            val dataStoreKey = preferencesKey<Int>(LAST_FRAGMENT)
            val preferences = dataStore.data.first()
            val result = preferences[dataStoreKey]

            if (result != null) {
                if (result >= 0 && result < fragments.size) {
                    when (result) {
                        1 -> navController.navigate(R.id.action_draft_navigation)
                        2 -> navController.navigate(R.id.action_match_navigation) //TODO Fix crash
                        3 -> {
                            navController.navigate(R.id.action_draft_navigation)
                            navController.navigate(R.id.action_registration_navigation)
                        }
                        4 -> {
                            navController.navigate(R.id.action_draft_navigation)
                            navController.navigate(R.id.action_login_navigation)
                        }
                        5 -> navController.navigate(R.id.action_club_navigation)
                        6 -> navController.navigate(R.id.action_statistics_navigation)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "fdsfsdf")
        }
    }

    private suspend fun saveToLastFragmentDataStore(fragmentId: Int) {
        val dataStoreKey = preferencesKey<Int>(LAST_FRAGMENT)
        dataStore.edit { fragments ->
            fragments[dataStoreKey] = fragmentId
        }
    }

    override fun onArrowNextClicked() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0] as FixturesFragment
        fragment.nextPage()
    }

    override fun onArrowBackClicked() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0] as FixturesFragment
        fragment.previousPage()
    }

    private fun handleDynamicLink() {

        firebaseDynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pdLinkData ->
                pdLinkData.let {
                    val oobCode: String? = pdLinkData.link?.getQueryParameter("oobCode")
                    oobCode?.let {
                        fAuth.checkActionCode(oobCode).addOnSuccessListener { result ->
                            when (result.operation) {
                                ActionCodeResult.VERIFY_EMAIL -> {
                                    fAuth.applyActionCode(oobCode)
                                        .addOnSuccessListener {
                                            fAuth.currentUser.let { user ->
                                                user?.reload()?.addOnSuccessListener {
                                                    if (user.isEmailVerified) {
                                                        reloadDraftFragment()
                                                    }
                                                }
                                            }
                                            Log.i(TAG, "Verified email")
                                        }
                                        .addOnFailureListener { resultCode ->
                                            Log.w(TAG, "Failed to ver   ify email", resultCode)
                                        }
                                }
                                ActionCodeResult.PASSWORD_RESET -> {
                                    Log.d(TAG, "PASSWORD RESET")
                                }
                            }
                        }.addOnFailureListener { result ->
                            Log.w(TAG, "Invalid code sent")
                        }
                    }
                }
            }.addOnFailureListener { ex ->
                Log.w(TAG, "Invalid code sent. $ex")
            }
    }

    private fun blockFragmentUi() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0]

        if (fragment is OnFragmentUiBlockListener) {
            listener = fragment
            listener?.blockUi()
        }
    }

    private fun reloadDraftFragment() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        navHostFragment.navController.navigate(R.id.action_draft_navigation)
    }

    private fun setSharedPreferencesTeamId(teamId: Int) {
        val sPref = this.getPreferences(MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putInt(TEAM_ID, teamId)
        ed?.apply()
    }

    override fun actionBarHide() {
        binding.appbar.visibility = View.GONE
    }

    override fun actionBarShow() {
        binding.appbar.visibility = View.VISIBLE
    }
}
