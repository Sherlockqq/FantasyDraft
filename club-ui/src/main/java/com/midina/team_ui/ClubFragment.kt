package com.midina.team_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavItemSelectListener
import com.midina.team_ui.databinding.FragmentClubBinding

class ClubFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_club

    private lateinit var binding: FragmentClubBinding
    private var listener: OnBottomNavItemSelectListener? = null

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
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        highlightIcon()
    }

    private fun highlightIcon() {
        if (context is OnBottomNavItemSelectListener) {
            listener = context as OnBottomNavItemSelectListener
            listener?.highlightItem(R.id.club_navigation)
        }
    }
}