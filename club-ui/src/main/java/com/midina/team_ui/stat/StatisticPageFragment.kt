package com.midina.team_ui.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.midina.core_ui.ui.BaseFragment
import com.midina.team_ui.R
import com.midina.team_ui.databinding.FragmentStatisticBinding

class StatisticPageFragment : BaseFragment() {

    private var binding: FragmentStatisticBinding? = null
    override val layoutId: Int = R.layout.fragment_statistic

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

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StatisticPageFragment()
    }
}