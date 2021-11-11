package com.midina.login_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.midina.login_ui.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect

class LoginFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_login

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory)[LoginViewModel::class.java]
    }

    private var listener: OnBottomNavHideListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBottomNavHideListener) {
            listener = context
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun onResume() {
        super.onResume()
        listener?.onHideBottomNavView()
    }

    override fun onStop() {
        super.onStop()
        listener?.onShowBottomNavView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.loginEvents
                .collect {
                    handleLoginEvents(it)
                }
        }

        binding.btSignIn.setOnClickListener {
            viewModel.signInClicked()
        }
        return binding.root
    }

    private fun handleLoginEvents(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnSigned -> {
                binding.tvRequirements.isGone = true
                findNavController().navigate(R.id.action_draft_navigation, null)
            }
            is LoginEvent.OnNotSigned -> {
                binding.tvRequirements.isVisible = true
            }
        }
    }
}