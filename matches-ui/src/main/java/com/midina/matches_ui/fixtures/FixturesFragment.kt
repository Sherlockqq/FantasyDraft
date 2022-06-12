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

class FixturesFragment : BaseFragment() {

    private val TAG = "FixturesFragment"

    override val layoutId = R.layout.fragment_fixtures

    private var binding: FragmentFixturesBinding? = null
    private lateinit var adapter: TourPageAdapter
    private var listener: OnBottomNavItemSelectListener? = null

    val viewModel: FixturesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FixturesViewModel::class.java]
    }

    @SuppressLint("ClickableViewAccessibility")
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

        lifecycleScope.launchWhenCreated {
            viewModel.events
                .collect {
                    handleEvents(it)
                }
        }

        return binding?.root
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

            setHasOptionsMenu(true)

            adapter = activity?.let {
                TourPageAdapter(it, matchesMap)
            }!!
            binding?.pager?.adapter = adapter
            binding?.pager?.setCurrentItem(viewModel.currentTour.value, false)
        }
    }

    private fun onError() {
        Log.d(TAG, "FixturesFragment OnError")
    }

    private fun onLoading() {
        Log.d(TAG, "FixturesFragment OnLoading")
    }

    private fun onEmptyState() {
        Log.d(TAG, "FixturesFragment OnEmptyState")
    }

    fun nextPage() {
        binding?.pager?.
        setCurrentItem(binding?.pager?.currentItem?.plus(1) ?: 1, true)
    }

    fun previousPage() {
        binding?.pager?.
        setCurrentItem(binding?.pager?.currentItem?.minus(1) ?: 1, true)
    }


    private fun highlightIcon() {
        if (context is OnBottomNavItemSelectListener) {
            listener = context as OnBottomNavItemSelectListener
            listener?.highlightItem(R.id.fixtures_navigation)
        }
    }
}