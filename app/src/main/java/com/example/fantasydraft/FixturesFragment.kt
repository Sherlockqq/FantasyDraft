package com.example.fantasydraft

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fantasydraft.databinding.FragmentFixturesBinding
import java.io.IOException

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class FixturesFragment : Fragment() {

    lateinit var binding: FragmentFixturesBinding

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


        binding.viewModel = viewModel



        return binding.root
    }


}