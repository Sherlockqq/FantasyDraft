package com.midina.draft_ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavItemSelectListener
import com.midina.core_ui.ui.OnFragmentUiBlockListener
import com.midina.draft_ui.databinding.FragmentDraftBinding
import kotlinx.coroutines.flow.collect

const val TAG = "DraftFragment"

class DraftFragment : BaseFragment(), OnFragmentUiBlockListener {

    override val layoutId = R.layout.fragment_draft

    private var binding: FragmentDraftBinding? = null
    val viewModel: DraftViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DraftViewModel::class.java]
    }
    private var listener: OnBottomNavItemSelectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_draft,
            container,
            false
        )

        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.viewModel = viewModel
        binding?.vwDraft?.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.signEvents
                .collect {
                    handleSignsEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.sendingEvent
                .collect {
                    handleSendingEvents(it)
                }
        }

        binding?.vwDraft?.btAction?.setOnClickListener {
            when (viewModel.signEvents.value) {
                SigningUiEvent.OnNotSignIn -> {
                    findNavController().navigate(R.id.action_registration_navigation)
                }
                is SigningUiEvent.OnNotVerified -> {
                    viewModel.verifyClicked()
                }
                is SigningUiEvent.OnVerified -> {
                    Log.d(TAG, "navigate to Play fragment")
                }
            }
        }
        binding?.vwDraft?.btSign?.setOnClickListener {
            when (viewModel.signEvents.value) {
                SigningUiEvent.OnNotSignIn -> {
                    findNavController().navigate(R.id.action_login_navigation, null)
                }
                is SigningUiEvent.OnNotVerified -> {
                    viewModel.signedOutClicked()
                }
                is SigningUiEvent.OnVerified -> {
                    viewModel.signedOutClicked()
                }
            }
        }
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        highlightIcon()
    }

    private fun handleSignsEvents(event: SigningUiEvent) {
        when (event) {
            is SigningUiEvent.OnVerified -> {
                binding?.vwDraft?.tvEmail?.text = getString(R.string.email, event.email)
                binding?.vwDraft?.tvEmail?.isVisible = true
            }
            is SigningUiEvent.OnNotSignIn -> {
                binding?.vwDraft?.tvEmail?.isGone = true
            }
            is SigningUiEvent.OnNotVerified -> {
                binding?.vwDraft?.tvEmail?.text = getString(R.string.verify_email, event.email)
                binding?.vwDraft?.tvEmail?.isVisible = true
            }
        }
    }

    private fun handleSendingEvents(event: SendingEvent) {
        when (event) {
            SendingEvent.OnDefault -> {
                Log.d(TAG, "DEFAULT")
            }
            SendingEvent.OnError -> {
                Toast.makeText(
                    context,
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            SendingEvent.OnSuccess -> {
                Toast.makeText(context, getString(R.string.email_is_sent), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun highlightIcon() {
        if (context is OnBottomNavItemSelectListener) {
            listener = context as OnBottomNavItemSelectListener
            listener?.highlightItem(R.id.draft_navigation)
        }
    }

    override fun blockUi() {
        binding?.vwDraft?.btAction?.isEnabled = false
        binding?.vwDraft?.btSign?.isEnabled = false
        binding?.tvVerifying?.isVisible = true
        binding?.pbVerifying?.isVisible = true
    }
}
