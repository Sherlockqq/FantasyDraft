package com.midina.draft_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.draft_ui.databinding.FragmentDraftBinding
import kotlinx.coroutines.flow.collect

class DraftFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_draft

    private lateinit var binding: FragmentDraftBinding
    val viewModel: DraftViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DraftViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_draft,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.signEvents
                .collect {
                    handleSignsEvents(it)
                }
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_registration_navigation, null)
        }
        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_navigation, null)
        }
        binding.signOutButton.setOnClickListener {
            viewModel.signedOutClicked()
        }
        return binding.root
    }

    private fun handleSignsEvents(event: SigningUiEvent) {
        when (event) {
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Draft", "OnDestroy")
    }
}
