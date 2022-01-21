package com.midina.engfixtures_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.midina.engfixtures_domain.model.Match
import java.util.ArrayList

import android.os.Parcelable
import android.util.Log


class TourPageAdapter(fa: FragmentActivity, matchesMap: Map<Int, ArrayList<Match>>) : FragmentStateAdapter(fa) {

    val matches = matchesMap


    override fun getItemCount(): Int {
        return matches.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = TourFragment()
        fragment.arguments = Bundle().apply {
            putInt("ARG_OBJECT", position + 1)
            putParcelableArrayList("r", matches[position+1])
            val matchess = matches[position+1]
            Log.d("FDSF", " matches $matchess")
        }
        return fragment
    }
}