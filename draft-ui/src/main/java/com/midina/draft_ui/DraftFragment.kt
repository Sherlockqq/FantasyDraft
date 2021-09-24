package com.midina.draft_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.viewModel = viewModel

        binding.registerButton.setOnClickListener { view ->
            findNavController().navigate(R.id.action_registration_navigation, null)
        }
//        binding.signInButton.setOnClickListener { view ->
//            findNavController().navigate(R.id.loginFragment,null)
//        }

        return binding.root
    }

}
