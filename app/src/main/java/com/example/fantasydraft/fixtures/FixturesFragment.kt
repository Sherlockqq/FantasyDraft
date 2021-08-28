package com.example.fantasydraft.fixtures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fantasydraft.match.MatchAdapter
import com.example.fantasydraft.match.MatchSchedule
import com.example.fantasydraft.R
import com.example.fantasydraft.databinding.FragmentFixturesBinding


//material sizes/ dimensions

//view pager
class FixturesFragment : Fragment() {

    lateinit var binding: FragmentFixturesBinding
    private val adapter = MatchAdapter()
    private val viewModel: FixturesViewModel by lazy {
        ViewModelProvider(this).get(FixturesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         binding = DataBindingUtil.inflate(
            inflater,
             R.layout.fragment_fixtures,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.fixturesList.layoutManager = LinearLayoutManager(this.context)

        viewModel.events.observe(viewLifecycleOwner, {handleEvents(it)})
        binding.fixturesList.adapter = adapter
        return binding.root
    }

    private fun handleEvents(event: UiEvent){
        when (event){
            is UiEvent.Success -> onSuccess(event.matches)
            is UiEvent.Error -> onError()
            is UiEvent.Loading -> onLoading()
        }
    }

    private fun onSuccess(event: List<MatchSchedule>) {
        binding.progressBar.isVisible = false
        binding.nonSuccessText.isVisible = false
        adapter.updateMatches(event)
    }

    private fun onError() {
        binding.nonSuccessText.text = "Connection Error"
        binding.progressBar.isVisible = false
        binding.nonSuccessText.isVisible = true
        binding.ConnectionErrorView.isVisible = true
    }

    private fun onLoading(){
        binding.nonSuccessText.text = "Loading.."
        binding.nonSuccessText.isVisible = true
        binding.progressBar.isVisible = true
    }
}