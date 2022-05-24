package com.midina.matches_ui.fixtures

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.midina.matches_ui.R
import com.midina.matches_ui.adapters.TourPageAdapter
import com.midina.matches_ui.databinding.FragmentFixturesBinding
import kotlinx.coroutines.flow.collect
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavItemSelectListener
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.AlarmReceiver

private const val SAVED_TOUR = "SAVED_TOUR"

class FixturesFragment : BaseFragment() {

    private val TAG = "FixturesFragment"

    override val layoutId = R.layout.fragment_fixtures

    private lateinit var binding: FragmentFixturesBinding
    private lateinit var adapter: TourPageAdapter
    private var listener: OnBottomNavItemSelectListener? = null

    val viewModel: FixturesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FixturesViewModel::class.java]
    }

    @SuppressLint("ClickableViewAccessibility")
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
            viewModel.events
                .collect {
                    handleEvents(it)
                }
        }

        return binding.root
    }

    @SuppressLint("NewApi")
    private fun handleEvents(event: UiEvent) {
        when (event) {
            is UiEvent.Success -> onSuccess(event.matches)
            is UiEvent.Error -> onError()
            is UiEvent.Loading -> onLoading()
            is UiEvent.EmptyState -> onEmptyState()
        }
    }

    private fun onSuccess(matchesMap: Map<Int, ArrayList<MatchSchedule>>) {
        if (matchesMap.isNotEmpty()) {
            Log.d("MainActivity", "list size : ${matchesMap.size}")
            binding.progressBar.isVisible = false
            binding.nonSuccessText.isVisible = false

            setHasOptionsMenu(true)

            adapter = activity?.let {
                TourPageAdapter(it, matchesMap)
            }!!
            binding.pager.adapter = adapter
            binding.pager.setCurrentItem(viewModel.currentTour.value, false)
            matchesMap[viewModel.currentTour.value]?.let { createAlarm(it) }
        }
    }

    private fun onError() {
        binding.nonSuccessText.setText(R.string.connection_error)
        binding.progressBar.isVisible = false
        binding.nonSuccessText.isVisible = true
        binding.ConnectionErrorView.isVisible = true
    }

    private fun onLoading() {
        binding.nonSuccessText.setText(R.string.loading)
        binding.nonSuccessText.isVisible = true
        binding.progressBar.isVisible = true
    }

    private fun onEmptyState() {
        binding.nonSuccessText.setText(R.string.empty_state)
        binding.progressBar.isVisible = false
        binding.nonSuccessText.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_first_tour -> binding.pager.setCurrentItem(1, false)

            R.id.show_current_tour -> binding.pager.setCurrentItem(
                viewModel.currentTour.value,
                false
            )
            else -> binding.pager.setCurrentItem(0, false)
        }
        return true
    }

    private fun MatchSchedule.toIntent(): Intent {
        val intent = Intent(activity?.applicationContext, AlarmReceiver::class.java)

        val bundle = Bundle()

        bundle.putInt("tour", tour)
        bundle.putInt("homeTeamId", id)
        bundle.putString("homeTeam", homeTeam)
        bundle.putString("guestTeam", guestTeam)

        intent.putExtras(bundle)
        return intent
    }

    private fun createAlarm(matchesList: ArrayList<MatchSchedule>) {
        val alarmManager =
            activity?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (index in matchesList.indices) {

            if (matchesList[index].score == getString(R.string.emptyScore)) {
                val intent = matchesList[index].toIntent()

                val pendingIntent = PendingIntent.getBroadcast(
                    activity?.applicationContext,
                    index,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        viewModel.getTimeInMillis(matchesList[index].date),
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        viewModel.getTimeInMillis(matchesList[index].date),
                        pendingIntent
                    )
                }
            }
        }
    }

    fun nextPage() {
        binding.pager.setCurrentItem(binding.pager.currentItem + 1, true)
    }

    fun previousPage() {
        binding.pager.setCurrentItem(binding.pager.currentItem - 1, true)
    }


    private fun highlightIcon() {
        if (context is OnBottomNavItemSelectListener) {
            listener = context as OnBottomNavItemSelectListener
            listener?.highlightItem(R.id.fixtures_navigation)
        }
    }
}