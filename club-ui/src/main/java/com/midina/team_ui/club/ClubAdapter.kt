package com.midina.team_ui.club

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.midina.team_ui.player.PlayersPageFragment
import com.midina.team_ui.stat.StatisticPageFragment

class ClubAdapter(fa: FragmentActivity, private val teamId: Int) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlayersPageFragment.newInstance(teamId)
            1 -> StatisticPageFragment.newInstance()
            else -> PlayersPageFragment.newInstance(teamId)
        }
    }
}