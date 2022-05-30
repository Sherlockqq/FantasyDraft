package com.midina.team_ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.midina.club_domain.model.fixtures.FixturesInfo
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavItemSelectListener
import com.midina.team_ui.databinding.FragmentClubBinding
import kotlinx.coroutines.flow.collect

private const val TAG = "ClubFragment"

//TODO TO FIX PROGRESS BAR. NOT IS GONE

class ClubFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_club

    private var binding: FragmentClubBinding? = null
    private var listener: OnBottomNavItemSelectListener? = null

    val viewModel: ClubViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ClubViewModel::class.java]
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

        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.viewModel = viewModel
        binding?.vTeamView?.viewModel = viewModel

        openSharedPref()

        lifecycleScope.launchWhenCreated {
            viewModel.teamId
                .collect {
                    handleTeamIdChanges(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.events
                .collect {
                    handleUiEvent(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.alarmEvents
                .collect {
                    handleAlarmEvent(it)
                }
        }

        binding?.vTeamView?.ivAlarm?.setOnClickListener {
            viewModel.setIsAlarm(null)
            viewModel.alarmClicked()
        }

        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        highlightIcon()
    }

    override fun onDestroy() {
        saveIsAlarmToSharedPref(viewModel.isAlarm.value)
        super.onDestroy()
    }

    private fun handleTeamIdChanges(teamId: Int) {
        if (teamId == 0) {
            val sPref = this.activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)
            val id = sPref?.getInt(FAVOURITE_TEAM_ID, 0)
            id?.let {
                viewModel.setTeamId(it)
            }
        } else {
            viewModel.dataRequest()
        }
    }

    private fun handleUiEvent(event: UiEvent) {
        when (event) {
            UiEvent.EmptyState -> Log.d(TAG, "UiEvent.EmptyState")
            UiEvent.Error -> Log.d(TAG, "UiEvent.Error")
            UiEvent.Loading -> {
                Log.d(TAG, "UiEvent.Loading")
                binding?.vTeamView?.vwTeamBackground?.isVisible = false
                binding?.vTeamView?.vwBackgroundNotification?.isVisible = false
            }
            is UiEvent.Success -> {
                binding?.pbLoading?.isGone = true // TODO Replace with data binding
                binding?.vTeamView?.vwTeamBackground?.isVisible = true
                binding?.vTeamView?.vwBackgroundNotification?.isVisible = true
                binding?.vTeamView?.tvTeamName?.text = event.teamInfo.team.name
                binding?.vTeamView?.tvStadiumName?.text = event.teamInfo.stadium.name
                binding?.vTeamView?.ivTeamLogo?.let {
                    Glide.with(this).load(event.teamInfo.team.logo).into(it)
                }
                binding?.vTeamView?.ivStadium?.let {
                    Glide.with(this).load(event.teamInfo.stadium.logo).into(it)
                }
            }
        }
    }

    private fun handleAlarmEvent(event: AlarmEvent) {
        when (event) {
            AlarmEvent.Default -> Log.d(TAG, "Alarm DEFAULT")
            AlarmEvent.EmptyState -> {
                showErrorToast()
            }
            AlarmEvent.Error -> {
                showErrorToast()
            }
            is AlarmEvent.Success -> {
                if (viewModel.isAlarm.value) {
                    createAlarm(event.list)
                } else {
                    cancelAlarm(event.list)
                }
            }
        }
    }

    private fun highlightIcon() {
        if (context is OnBottomNavItemSelectListener) {
            listener = context as OnBottomNavItemSelectListener
            listener?.highlightItem(R.id.club_navigation)
        }
    }

    private fun FixturesInfo.toIntent(): Intent {
        val intent = Intent(activity?.applicationContext, AlarmReceiver::class.java)

        val bundle = Bundle()

        bundle.putString(TOUR, tour)
        bundle.putString(HOME, home)
        bundle.putString(AWAY, away)

        intent.putExtras(bundle)
        return intent
    }

    private fun createAlarm(matchesList: ArrayList<FixturesInfo>) {
        val alarmManager =
            activity?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (index in matchesList.indices) {

            val intent = matchesList[index].toIntent()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    viewModel.getTimeInMillis(matchesList[index].date),
                    getPendingCreateAlarmIdleIntent(index, intent)
                )
            } else {

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    viewModel.getTimeInMillis(matchesList[index].date),
                    getPendingAlarmIntent(index, intent)
                )
            }
        }
    }

    private fun cancelAlarm(matchesList: ArrayList<FixturesInfo>) {
        val alarmManager =
            activity?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (index in matchesList.indices) {
            val intent = matchesList[index].toIntent()
            val pending: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingCreateAlarmIdleIntent(index, intent)
            } else {
                getPendingAlarmIntent(index, intent)
            }
            alarmManager.cancel(pending)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPendingCreateAlarmIdleIntent(index: Int, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            activity?.applicationContext,
            index,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingAlarmIntent(index: Int, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            activity?.applicationContext,
            index,
            intent,
            0
        )
    }

    private fun saveIsAlarmToSharedPref(isAlarm: Boolean) {
        val sPref = this.activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putBoolean(IS_ALARM, isAlarm)
        ed?.apply()
    }

    private fun openSharedPref() {
        val sPref = this.activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val sPrefIsAlarm = sPref?.getBoolean(IS_ALARM, false)
        viewModel.setIsAlarm(sPrefIsAlarm)
    }

    private fun showErrorToast() {
        viewModel.setIsAlarm(false)
        Toast.makeText(
            context,
            getString(com.midina.core_ui.R.string.something_went_wrong),
            Toast.LENGTH_SHORT
        ).show()

    }

    companion object {
        const val TOUR = "TOUR"
        const val HOME = "HOME"
        const val AWAY = "AWAY"
        const val IS_ALARM = "IS_ALARM"
    }
}
