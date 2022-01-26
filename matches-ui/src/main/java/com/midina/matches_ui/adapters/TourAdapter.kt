package com.midina.matches_ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.tour.TourFragment

class TourPageAdapter(fa: FragmentActivity, matchesMap: Map<Int, ArrayList<MatchSchedule>>) :
    FragmentStateAdapter(fa){

    private val TAG = "TourPageAdapter"
    val matches = matchesMap

    override fun getItemCount(): Int {
        return matches.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = TourFragment()
        fragment.arguments = Bundle().apply {
            putParcelableArrayList(KEY, matches[position])
        }
        return fragment
    }

    companion object {
        const val KEY = "MATCHES"
    }
}