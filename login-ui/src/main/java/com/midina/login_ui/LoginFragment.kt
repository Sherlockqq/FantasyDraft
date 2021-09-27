package com.midina.login_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.midina.login_ui.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment() {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory )[LoginViewModel::class.java] }
    private var listener : OnBottomNavHideListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnBottomNavHideListener){
            listener = context
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.onHide()
    }

    override fun onStop() {
        super.onStop()
        listener?.onShow()
    }

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