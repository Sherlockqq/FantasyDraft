package com.midina.favourite_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnStartActivityListener
import com.midina.favourite_domain.model.Team
import com.midina.favourite_ui.databinding.FragmentFavouriteBinding
import kotlinx.coroutines.flow.collect

const val TAG = "FavouriteFragment"

class FavouriteFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_favourite
    private val adapter = FavouriteAdapter()

    private var binding: FragmentFavouriteBinding? = null
    private var listenerOn: OnStartActivityListener? = null

    val viewModel: FavouriteViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]
    }

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

        binding?.rvTeams?.layoutManager = LinearLayoutManager(this.context)

        binding?.rvTeams?.adapter = adapter

        adapter.setOnItemClickListener(object : FavouriteAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, team: Team) {
                setSharedPreferencesData(team)
            }
        })

        lifecycleScope.launchWhenCreated {
            viewModel.events
                .collect {
                    handleEvents(it)
                }
        }
        return binding?.root
    }

    private fun handleEvents(event: UiEvent) {
        when (event) {
            is UiEvent.Success -> onSuccess(event.team)
            is UiEvent.Error -> onError()
            is UiEvent.Loading -> onLoading()
            is UiEvent.EmptyState -> onEmptyState()
        }
    }

    private fun onSuccess(list: List<Team>) {
        if (list.isNotEmpty()) {
            adapter.updateTeams(list)
        }
    }

    private fun onError() {
        Log.d(TAG, "onError")

    }

    private fun onLoading() {
        Log.d(TAG, "onLoading")

    }

    private fun onEmptyState() {
        Log.d(TAG, "onEmptyState")
    }

    @SuppressLint("CommitPrefEdits")
    private fun setSharedPreferencesData(team: Team) {

        val sPref = this.activity?.getPreferences(AppCompatActivity.MODE_PRIVATE)
        val ed = sPref?.edit()
        ed?.putInt(TEAM_ID, team.id)
        ed?.putString(FAVOURITE_TEAM_LOGO, team.logo)
        ed?.apply()
        navigateToMainActivity(team.id, team.logo)
    }

    private fun navigateToMainActivity(teamId: Int, teamLogo: String) {
        if (context is OnStartActivityListener) {
            listenerOn = context as OnStartActivityListener
            listenerOn?.startMainActivity(teamId, teamLogo)
        }
    }
}