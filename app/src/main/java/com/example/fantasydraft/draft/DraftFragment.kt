package com.example.fantasydraft.draft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import com.example.fantasydraft.R
import com.example.fantasydraft.databinding.FragmentDraftBinding


class DraftFragment : Fragment() {

    private lateinit var binding: FragmentDraftBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_draft,
            container,
            false)

        binding.registerButton.setOnClickListener { view ->
            findNavController().navigate(R.id.registrationFragment, null)
        }

        return binding.root
    }

}
