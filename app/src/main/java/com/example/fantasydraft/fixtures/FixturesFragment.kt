package com.example.fantasydraft.fixtures

import android.os.Bundle
import android.view.*
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
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

        setHasOptionsMenu(true)

        viewModel.tours.observe(viewLifecycleOwner,{
            if(it != 0 ){
                binding.gameweekText.text = "GameWeek $it"
            }else{
                binding.gameweekText.text = "Schedule"
            }
        })

        viewModel.events.observe(viewLifecycleOwner, {handleEvents(it)})
        binding.fixturesList.adapter = adapter

        binding.backArrow.setOnClickListener {
            viewModel.backArrowClicked()
        }

        binding.nextArrow.setOnClickListener {
           viewModel.nextArrowClicked()
        }

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
        when(viewModel.tours.value){
            0 -> {
                binding.backArrow.isInvisible = true
                binding.nextArrow.isVisible = true
            }
            30 ->{
                binding.nextArrow.isInvisible = true
                binding.backArrow.isVisible = true
            }
            else -> {
                binding.backArrow.isVisible = true
                binding.nextArrow.isVisible = true
            }
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_first_tour -> FixturesViewModel.TourFilter.SHOW_FIRST
                R.id.show_second_tour -> FixturesViewModel.TourFilter.SHOW_SECOND
                else -> FixturesViewModel.TourFilter.SHOW_ALL
            }
        )
        return true
    }
}
