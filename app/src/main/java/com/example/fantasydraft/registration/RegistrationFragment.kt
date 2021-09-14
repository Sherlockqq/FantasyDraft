package com.example.fantasydraft.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fantasydraft.R
import com.example.fantasydraft.databinding.RegistrationFragmentBinding

//TODO Correct Date input
//TODO Only One CheckBox Is Available
//TODO Progress Bar with Password input
//TODO Correct Email input
//TODO Check everything is put and correctly
//TODO Correct Password input
//TODO Correct showing CustomView

//TODO Show layout
class RegistrationFragment: Fragment() {

    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registration_fragment,
            container,
            false)

        return binding.root
    }

}