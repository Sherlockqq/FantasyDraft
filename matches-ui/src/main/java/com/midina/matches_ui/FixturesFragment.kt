package com.midina.matches_ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.midina.core_ui.ui.BaseFragment
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.databinding.FragmentFixturesBinding
import kotlinx.coroutines.flow.collect

class FixturesFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_fixtures

    private lateinit var binding: FragmentFixturesBinding
    private val adapter = MatchAdapter()

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

        binding.fixturesList.layoutManager = LinearLayoutManager(this.context)

        setHasOptionsMenu(true)

        setGameText()

        lifecycleScope.launchWhenCreated {
            viewModel.events
                .collect {
                    handleEvents(it)
                }
        }

        binding.fixturesList.adapter = adapter
        adapter.setOnItemClickListener(object : MatchAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, match: MatchSchedule) {
                findNavController().navigate(R.id.action_match_navigation, match.toBundle())
            }
        })

        binding.backArrow.setOnClickListener {
            viewModel.backArrowClicked()
        }

        binding.nextArrow.setOnClickListener {
            viewModel.nextArrowClicked()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setGameText() {
        if (viewModel.tours.value != 0) {
            binding.gameweekText.text = "Тур ${viewModel.tours.value}"
        } else {
            binding.gameweekText.setText(R.string.schedule)
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onSuccess(list: List<MatchSchedule>) {
        if (list.isNotEmpty()) {
            setGameText()
            Log.d("MainActivity", "list size : ${list.size}")
            binding.progressBar.isVisible = false
            binding.nonSuccessText.isVisible = false
            binding.gameweekText.isVisible = true
            when (viewModel.tours.value) {
                0 -> {
                    binding.backArrow.isInvisible = true
                    binding.nextArrow.isVisible = true
                }
                30 -> {
                    binding.nextArrow.isInvisible = true
                    binding.backArrow.isVisible = true
                    isCurrentTour(list)
                }
                else -> {
                    binding.backArrow.isVisible = true
                    binding.nextArrow.isVisible = true
                    isCurrentTour(list)
                }
            }
            adapter.updateMatches(list)
        }
    }

    private fun onError() {
        binding.nonSuccessText.setText(R.string.connection_error)
        binding.progressBar.isVisible = false
        binding.nextArrow.isVisible = false
        binding.backArrow.isVisible = false
        binding.gameweekText.isVisible = false
        binding.nonSuccessText.isVisible = true
        binding.ConnectionErrorView.isVisible = true
    }

    private fun onLoading() {
        binding.nonSuccessText.setText(R.string.loading)
        binding.nonSuccessText.isVisible = true
        binding.progressBar.isVisible = true
        binding.nextArrow.isVisible = false
        binding.backArrow.isVisible = false
        binding.gameweekText.isVisible = false
    }

    private fun onEmptyState() {
        binding.nonSuccessText.setText(R.string.empty_state)
        binding.progressBar.isVisible = false
        binding.nextArrow.isVisible = false
        binding.backArrow.isVisible = false
        binding.gameweekText.isVisible = false
        binding.nonSuccessText.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_first_tour -> FixturesViewModel.TourFilter.SHOW_FIRST
                R.id.show_second_tour -> FixturesViewModel.TourFilter.SHOW_SECOND
                else -> FixturesViewModel.TourFilter.SHOW_ALL
            }
        )
        return true
    }

    private fun MatchSchedule.toBundle() =
        Bundle().also {
            it.putString("HomeTeam", this.homeTeam)
            it.putString("GuestTeam", this.guestTeam)
            it.putString("Score", this.score)
            it.putString("Date", this.date)
        }

    private fun MatchSchedule.toIntent(
        tour: Int,
        homeTeam: String,
        guestTeam: String
    ): Intent {
        val intent = Intent(activity?.applicationContext, AlarmReceiver::class.java)

        val bundle = Bundle()

        bundle.putInt("tour", tour)
        bundle.putString("homeTeam", homeTeam)
        bundle.putString("guestTeam", guestTeam)

        intent.putExtras(bundle)
        return intent
    }

    private fun createAlarm(matchesList: List<MatchSchedule>) {
        val alarmManager =
            activity?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (index in matchesList.indices) {

            if (matchesList[index].score == "? : ?") {
                val intent = matchesList[index].toIntent(
                    matchesList[index].tour,
                    matchesList[index].homeTeam,
                    matchesList[index].guestTeam
                )

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
                    Log.d("FixtureFragment", "while idle")
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        viewModel.getTimeInMillis(matchesList[index].date),
                        pendingIntent
                    )
                    Log.d("FixtureFragment", "not idle")

                }
            }
        }
    }

    private fun isCurrentTour(list: List<MatchSchedule>) {
        if (viewModel.tours.value == viewModel.currentTour.value) {
            createAlarm(list)
        }
    }
}