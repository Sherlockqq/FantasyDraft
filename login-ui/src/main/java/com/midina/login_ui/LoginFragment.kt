package com.midina.login_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.midina.core_ui.ui.BaseFragment
import com.midina.login_ui.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment() {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory )[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.btSignIn.setOnClickListener {
            viewModel.signInClicked()
        }

        return binding.root
    }

}