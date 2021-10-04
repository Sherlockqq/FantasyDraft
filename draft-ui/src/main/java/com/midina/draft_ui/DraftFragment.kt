package com.midina.draft_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.draft_ui.databinding.FragmentDraftBinding


class DraftFragment : BaseFragment() {

    private lateinit var binding: FragmentDraftBinding
    val viewModel: DraftViewModel by lazy {
        ViewModelProvider(this, viewmodelFactory )[DraftViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_draft,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModel.signEvents.observe(viewLifecycleOwner,{handleSignsEvents(it)})

        binding.registerButton.setOnClickListener { view ->
            findNavController().navigate(R.id.action_registration_navigation, null)
        }
        binding.signInButton.setOnClickListener { view ->
            findNavController().navigate(R.id.action_login_navigation,null)
        }
        binding.signOutButton.setOnClickListener { view ->
            viewModel.signedOutClicked()
        }
        return binding.root
    }

    private fun handleSignsEvents(event: SigningUiEvent){
        when (event){
            is SigningUiEvent.onSignIn -> {
                binding.signInButton.isInvisible = true
                binding.signOutButton.isVisible = true
                binding.btPlay.isEnabled = true
            }
            is SigningUiEvent.onNotSignIn -> {
                binding.signInButton.isVisible = true
                binding.signOutButton.isInvisible = true
                binding.btPlay.isEnabled = false
            }
        }
    }
}
