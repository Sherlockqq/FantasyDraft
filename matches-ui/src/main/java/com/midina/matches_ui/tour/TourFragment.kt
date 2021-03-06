package com.midina.matches_ui.tour

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.midina.core_ui.ui.BaseFragment
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.OnArrowClickListener
import com.midina.matches_ui.R
import com.midina.matches_ui.adapters.MatchAdapter
import com.midina.matches_ui.adapters.TourPageAdapter.Companion.KEY_MATCHES
import com.midina.matches_ui.adapters.TourPageAdapter.Companion.KEY_TOURS
import com.midina.matches_ui.databinding.FragmentTourBinding

class TourFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_tour

    private val TAG = "TourFragment"

    private lateinit var binding: FragmentTourBinding
    private val adapter = MatchAdapter()

    private var listener: OnArrowClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "context $context")
        Log.d(TAG, "parentFragment $parentFragment")
        if (context is OnArrowClickListener) {
            listener = context
        } else {
            throw IllegalArgumentException()
        }
    }


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

        binding.rvTourList.layoutManager = LinearLayoutManager(this.context)

        binding.rvTourList.adapter = adapter

        arguments?.takeIf { it.containsKey(KEY_MATCHES) }?.apply {
            val matches = getParcelableArrayList<MatchSchedule>(KEY_MATCHES)
            val tours = getInt(KEY_TOURS)
            Log.d(TAG, "Matches: $matches")
            if (matches != null) {
                adapter.updateMatches(matches)
                binding.gameweekText.text = getString(R.string.gameweek, matches[1].tour)
                setArrows(matches[1].tour, tours)
            }
        }

        adapter.setOnItemClickListener(object : MatchAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, match: MatchSchedule) {
                findNavController().navigate(R.id.action_match_navigation, match.toBundle())
            }
        })

        binding.nextArrow.setOnClickListener {
            listener?.onArrowNextClicked()
        }

        binding.backArrow.setOnClickListener {
            listener?.onArrowBackClicked()
        }

        return binding.root
    }

    private fun MatchSchedule.toBundle() =
        Bundle().also {
            it.putInt("fixtureId", fixtureId)
        }

    private fun setArrows(tour: Int, lastTour: Int) {
        when (tour) {
            1 -> {
                binding.backArrow.isVisible = false
                binding.nextArrow.isVisible = true
            }
            lastTour -> {
                binding.backArrow.isVisible = true
                binding.nextArrow.isVisible = false
            }
            else -> {
                binding.backArrow.isVisible = true
                binding.nextArrow.isVisible = true
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(list: ArrayList<MatchSchedule>?, tour: Int) =
            TourFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_MATCHES, list)
                    putInt(KEY_TOURS, tour)
                }
            }
    }
}