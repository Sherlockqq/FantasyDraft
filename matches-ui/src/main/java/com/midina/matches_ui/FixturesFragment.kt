package com.midina.matches_ui

import android.os.Bundle
import android.view.*
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.midina.core_ui.ui.BaseFragment
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.databinding.FragmentFixturesBinding
import com.midina.matches_ui.fixtures.FixturesViewModel
import com.midina.matches_ui.fixtures.UiEvent
import javax.inject.Inject

class FixturesFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_fixtures

    private lateinit var binding: FragmentFixturesBinding
    private val adapter = MatchAdapter()

    val viewModel: FixturesViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory)[FixturesViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fixtures,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.fixturesList.layoutManager = LinearLayoutManager(this.context)

        setHasOptionsMenu(true)

        setGameText()

        viewModel.events.observe(viewLifecycleOwner, { handleEvents(it) })
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

    private fun setGameText() {
        if (viewModel.tours.value != 0) {
            binding.gameweekText.text = "Тур ${viewModel.tours.value}"
        } else {
            binding.gameweekText.setText(R.string.schedule)
        }
    }

    private fun handleEvents(event: UiEvent) {
        when (event) {
            is UiEvent.Success -> onSuccess(event.matches)
            is UiEvent.Error -> onError()
            is UiEvent.Loading -> onLoading()
            is UiEvent.EmptyState -> onEmptyState()
        }
    }

    private fun onSuccess(event: List<MatchSchedule>) {
        if (event.isNotEmpty()) {
            setGameText()

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
                }
                else -> {
                    binding.backArrow.isVisible = true
                    binding.nextArrow.isVisible = true
                }
            }
            adapter.updateMatches(event)
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
}
