package com.midina.engfixtures_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.midina.core_ui.ui.BaseFragment
import com.midina.engfixtures_domain.model.Match
import com.midina.engfixtures_ui.databinding.FragmentTourBinding


class TourFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_tour

    private lateinit var binding: FragmentTourBinding
    private  val TAG = "TourFragment"

    private val adapter = TourListAdapter()
//    val viewModel: EngFixturesViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[EngFixturesViewModel::class.java]
//    }

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

      //  binding.viewModel = viewModel
        binding.rvTour.layoutManager = LinearLayoutManager(this.context)

        binding.rvTour.adapter = adapter
        arguments?.takeIf { it.containsKey("ARG_OBJECT") }?.apply {
            binding.tvTour.text = getInt("ARG_OBJECT").toString()
            val matches = getParcelableArrayList<Match>("r")
            Log.d(TAG,"Matches: $matches")
            if (matches != null) {
                adapter.updateMatches(matches)
            }
        }

        return binding.root
    }
}