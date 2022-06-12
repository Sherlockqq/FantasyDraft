package com.midina.team_ui.player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midina.core_ui.ui.BaseFragment
import com.midina.team_ui.R
import com.midina.team_ui.databinding.FragmentPlayersPageBinding
import kotlinx.coroutines.flow.collect

private const val TAG = "PlayersPageFragment"
class PlayersPageFragment : BaseFragment() {

    private var binding: FragmentPlayersPageBinding? = null
    override val layoutId = R.layout.fragment_players_page
    private val adapter = PlayersAdapter()

    private val viewModel: PlayersPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlayersPageViewModel::class.java]
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
        binding?.rvPlayers?.layoutManager = LinearLayoutManager(this.context)
        binding?.rvPlayers?.adapter = adapter
        binding?.rvPlayers?.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager(this.context).orientation
            )
        )

        lifecycleScope.launchWhenCreated {
            viewModel.playersEvents
                .collect {
                    handlePlayersEvents(it)
                }
        }

        return binding?.root
    }

    private fun handlePlayersEvents(event: PlayerUiEvent) {
        when (event) {
            PlayerUiEvent.EmptyState -> Log.d(TAG, "EmptyState")
            PlayerUiEvent.Error ->  Log.d(TAG, "Error")
            PlayerUiEvent.Loading ->  Log.d(TAG, "Error")
            is PlayerUiEvent.Success -> {
                adapter.updatePlayers(event.players)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(teamId: Int) =
            PlayersPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(TEAM_ID, teamId)
                }
            }
    }
}