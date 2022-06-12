package com.midina.matches_ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.tour.TourFragment

class TourPageAdapter(fa: FragmentActivity, val matchesMap: Map<Int, ArrayList<MatchSchedule>>) :
    FragmentStateAdapter(fa) {

    private val TAG = "TourPageAdapter"
    override fun getItemCount(): Int {
        return matchesMap.size
    }

    override fun createFragment(position: Int): Fragment {
        return TourFragment.newInstance(matchesMap[position + 1], matchesMap.size)
    }

    companion object {
        const val KEY_MATCHES = "MATCHES"
        const val KEY_TOURS = "TOURS"
    }
}