package com.midina.registration_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.midina.registration_ui.databinding.RegistrationFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

const val TAG = "RegistrationFragment"

class RegistrationFragment : BaseFragment() {

    override val layoutId = R.layout.registration_fragment

    private var binding: RegistrationFragmentBinding? = null

    private val viewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
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
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        binding?.lifecycleOwner = viewLifecycleOwner

        binding?.firstName?.viewModel = viewModel
        binding?.lastName?.viewModel = viewModel
        binding?.email?.viewModel = viewModel
        binding?.password?.viewModel = viewModel
        binding?.gender?.viewModel = viewModel
        binding?.date?.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.firstNameEvents
                .collect {
                    handleFirstNameEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.lastNameEvents
                .collect {
                    handleLastNameEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.emailEvents
                .collect {
                    handleEmailEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.passwordEvents
                .collect {
                    handlePasswordEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.daysEvents
                .collect {
                    handleDaysEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.monthesEvents
                .collect {
                    handleMonthesEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.registerEvents
                .collect {
                    handleRegistrationEvents(it)
                }
        }

        binding?.date?.etDateDays?.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding?.date?.etDateDays?.text?.length == 1) {
                    binding?.date?.etDateDays?.setText(
                        getString(
                            R.string.edit_date,
                            binding?.date?.etDateDays?.text
                        )
                    )
                }
                binding?.date?.etDateMonthes?.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding?.date?.etDateMonthes?.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding?.date?.etDateMonthes?.text?.length == 1) {
                    binding?.date?.etDateMonthes?.setText(
                        getString(
                            R.string.edit_date,
                            binding?.date?.etDateMonthes?.text
                        )
                    )
                }
                binding?.date?.etDateYears?.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })


        binding?.gender?.cbMale?.setOnClickListener {
            maleChecked()
        }

        binding?.gender?.cbFemale?.setOnClickListener {
            femaleChecked()
        }

        binding?.gender?.cbUnspecified?.setOnClickListener {
            unspecifiedChecked()
        }

        binding?.btRegist?.setOnClickListener {
            binding?.btRegist?.isEnabled = false
            viewModel.registrationIsClicked()
        }

        return binding?.root
    }

    private fun handleFirstNameEvents(event: FirstNameUiEvent) {
        if (event is FirstNameUiEvent.OnTextValid) {
            binding?.firstName?.tvRequest?.isGone = true
        } else if (event is FirstNameUiEvent.OnTextInvalid) {
            binding?.firstName?.tvRequest?.isVisible = true
        }
    }

    private fun handleLastNameEvents(event: LastNameUiEvent) {
        if (event is LastNameUiEvent.OnTextValid) {
            binding?.lastName?.tvRequest?.isGone = true
        } else if (event is LastNameUiEvent.OnTextInvalid) {
            binding?.lastName?.tvRequest?.isVisible = true
        }
    }

    private fun handleEmailEvents(event: EmailUiEvent) {
        if (event is EmailUiEvent.OnTextValid) {
            binding?.email?.tvRequest?.isGone = true
        } else if (event is EmailUiEvent.OnTextInvalid) {
            binding?.email?.tvRequest?.isVisible = true
        }
    }

    private fun handlePasswordEvents(event: PasswordUiEvent) {
        when (event) {
            is PasswordUiEvent.OnTextEmpty -> {
                binding?.password?.passProgressBar?.isGone = true
            }
            is PasswordUiEvent.OnTextValid -> {
                binding?.password?.tvRequest?.isGone = true
                val max = 8
                binding?.password?.passProgressBar?.progress = max
            }
            is PasswordUiEvent.OnTextInvalid -> {
                binding?.password?.passProgressBar?.isGone = true
                binding?.password?.tvRequest?.isVisible = true

            }
            is PasswordUiEvent.OnProcess -> {
                binding?.password?.passProgressBar?.isVisible = true
                binding?.password?.passProgressBar?.progress = event.textSize
            }
        }
    }

    private fun handleDaysEvents(event: DaysUiEvent) {
        if (event is DaysUiEvent.OnFinish) {
            binding?.date?.etDateMonthes?.requestFocus()
        } else if (event is DaysUiEvent.OnNotFocus) {
            if (binding?.date?.etDateDays?.text?.length == 1) {
                binding?.date?.etDateDays?.removeTextChangedListener(
                    viewModel.daysOnTextChangeListener
                )
                binding?.date?.etDateDays?.setText(
                    getString(
                        R.string.edit_date,
                        binding?.date?.etDateDays?.text
                    )
                )
                binding?.date?.etDateDays?.addTextChangedListener(
                    viewModel.daysOnTextChangeListener
                )
            }
        }
    }

    private fun handleMonthesEvents(event: MonthesUiEvent) {
        if (event is MonthesUiEvent.OnFinish) {
            binding?.date?.etDateYears?.requestFocus()
        } else if (event is MonthesUiEvent.OnNotFocus) {
            if (binding?.date?.etDateMonthes?.text?.length == 1) {
                binding?.date?.etDateMonthes?.removeTextChangedListener(
                    viewModel.monthesOnTextChangeListener
                )
                binding?.date?.etDateMonthes?.setText(
                    getString(
                        R.string.edit_date,
                        binding?.date?.etDateMonthes?.text
                    )
                )
                binding?.date?.etDateMonthes?.addTextChangedListener(
                    viewModel.monthesOnTextChangeListener
                )
            }
        }
    }

    private fun handleRegistrationEvents(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.OnSuccess -> {
                findNavController().navigate(R.id.action_draft_navigation, null)
            }
            is RegistrationEvent.OnError -> {
                binding?.pbLoading?.isVisible = false
                binding?.btRegist?.isVisible = true
                binding?.btRegist?.isEnabled = true

                when (event.error) {
                    getString(R.string.error_collision) -> {
                        viewModel.setEmailError()
                        Toast.makeText(context, event.error, LENGTH_LONG).show()
                    }
                    getString(R.string.error_connection) -> {
                        Toast
                            .makeText(context, getString(R.string.toast_connection), LENGTH_LONG)
                            .show()
                    }
                    getString(R.string.error_data) -> {
                        Toast.makeText(context, event.error, LENGTH_LONG).show()
                    }
                }
            }
            is RegistrationEvent.OnProgress -> {
                binding?.btRegist?.isVisible = false
                binding?.pbLoading?.isVisible = true
            }
        }
    }

    private fun maleChecked() {
        binding?.gender?.cbMale?.isClickable = false

        binding?.gender?.cbFemale?.isClickable = true
        binding?.gender?.cbFemale?.isChecked = false

        binding?.gender?.cbUnspecified?.isClickable = true
        binding?.gender?.cbUnspecified?.isChecked = false

        viewModel.maleClicked()
    }

    private fun femaleChecked() {
        binding?.gender?.cbFemale?.isClickable = false

        binding?.gender?.cbMale?.isClickable = true
        binding?.gender?.cbMale?.isChecked = false

        binding?.gender?.cbUnspecified?.isClickable = true
        binding?.gender?.cbUnspecified?.isChecked = false

        viewModel.femaleClicked()
    }

    private fun unspecifiedChecked() {
        binding?.gender?.cbUnspecified?.isClickable = false

        binding?.gender?.cbMale?.isClickable = true
        binding?.gender?.cbMale?.isChecked = false

        binding?.gender?.cbFemale?.isClickable = true
        binding?.gender?.cbFemale?.isChecked = false

        viewModel.unspecifiedClicked()
    }
}