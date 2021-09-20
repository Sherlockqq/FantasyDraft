package com.example.fantasydraft.registration

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fantasydraft.R
import com.example.fantasydraft.databinding.RegistrationFragmentBinding
import com.example.fantasydraft.utils.State
import java.lang.NumberFormatException
import android.view.inputmethod.EditorInfo

import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.doAfterTextChanged


//TODO Correct Date input
//TODO Check everything is put and correctly
//TODO Correct showing CustomView
//TODO Make ProgressBar Beauty
//TODO Disable Putting not number in Date EditText`s
//TODO One char in Days or Monthes than unfocused
//TODO Make CustomView correct size( test on physical device)
//TODO Make Year listeners in VM
//TODO Make CV Date


class RegistrationFragment: Fragment() {

    private lateinit var binding: RegistrationFragmentBinding
    private val viewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registration_fragment,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModel.firstNameEvents.observe(viewLifecycleOwner,{handleFirstNameEvents(it)})
        viewModel.lastNameEvents.observe(viewLifecycleOwner, {handleLastNameEvents(it)})
        viewModel.emailEvents.observe(viewLifecycleOwner,{handleEmailEvents(it)})
        viewModel.passwordEvents.observe(viewLifecycleOwner,{handlePasswordEvents(it)})
        viewModel.daysEvents.observe(viewLifecycleOwner,{handleDaysEvents(it)})
        viewModel.monthesEvents.observe(viewLifecycleOwner,{handleMonthesEvents(it)})

        binding.etDateDays.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(binding.etDateDays.text.length == 1){
                    binding.etDateDays.setText("0${binding.etDateDays.text}")
                }
                binding.etDateMonthes.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.etDateMonthes.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(binding.etDateMonthes.text.length == 1){
                    binding.etDateMonthes.setText("0${binding.etDateMonthes.text}")
                }
                binding.etDateYears.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        binding.etDateMonthes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s?.length == 2){
                    binding.etDateYears.requestFocus()
                }
                val monthesNum: Int
                try{
                    monthesNum  = s.toString().toInt()
                    if(monthesNum in 1..12){
                        binding.tvDateRq.text = "True"
                    }else{
                        binding.tvDateRq.text = "False"
                    }
                }catch (e: NumberFormatException){
                    binding.tvDateRq.text = "False"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }
        })

        binding.cbMale.setOnClickListener{
            binding.cbMale.isClickable = false

            binding.cbFemale.isClickable = true
            binding.cbFemale.isChecked = false

            binding.cbUnspecified.isClickable = true
            binding.cbUnspecified.isChecked = false

            binding.cvGender.setState(State.CORRECT)
            viewModel.genderState = State.CORRECT
        }

        binding.cbFemale.setOnClickListener{
            binding.cbFemale.isClickable = false

            binding.cbMale.isClickable = true
            binding.cbMale.isChecked = false

            binding.cbUnspecified.isClickable = true
            binding.cbUnspecified.isChecked = false
            binding.cvGender.setState(State.CORRECT)
            viewModel.genderState = State.CORRECT

        }

        binding.cbUnspecified.setOnClickListener{
            binding.cbUnspecified.isClickable = false

            binding.cbMale.isClickable = true
            binding.cbMale.isChecked = false

            binding.cbFemale.isClickable = true
            binding.cbFemale.isChecked = false
            binding.cvGender.setState(State.CORRECT)
            viewModel.genderState = State.CORRECT

        }

        return binding.root
    }

    private fun handleFirstNameEvents(event: FirstNameUiEvent){
        when (event){
            is FirstNameUiEvent.OnTextEmpty -> {
                binding.tvNameRequest.isVisible = true
                binding.cvFirstName.setState(State.ERROR)
            }
            is FirstNameUiEvent.OnTextValid -> {
                binding.tvNameRequest.isGone = true
                binding.cvFirstName.setState(State.CORRECT)
            }
        }
    }

    private fun handleLastNameEvents(event: LastNameUiEvent){
        when (event){
            is LastNameUiEvent.OnTextEmpty -> {
                binding.tvLastNameRequest.isVisible = true
                binding.cvLastname.setState(State.ERROR)
            }
            is LastNameUiEvent.OnTextValid -> {
                binding.tvLastNameRequest.isGone = true
                binding.cvLastname.setState(State.CORRECT)
            }
        }
    }

    private fun handleEmailEvents(event: EmailUiEvent){
        when (event){
            is EmailUiEvent.OnTextEmpty -> {
                binding.tvEmailRequest.isVisible = true
                binding.cvEmail.setState(State.ERROR)
            }
            is EmailUiEvent.OnTextValid -> {
                binding.tvEmailRequest.isGone = true
                binding.cvEmail.setState(State.CORRECT)
            }
            is EmailUiEvent.OnTextInvalid ->{
                binding.tvEmailRequest.isVisible = true
                binding.cvEmail.setState(State.ERROR)
            }
        }
    }

    private fun handlePasswordEvents(event: PasswordUiEvent){
        when (event){
            is PasswordUiEvent.OnTextEmpty -> {
                binding.passProgressBar.isGone = true
                binding.cvPassword.setState(State.ERROR)
            }
            is PasswordUiEvent.OnTextValid -> {
                binding.tvPassRequirements.isGone = true
                binding.cvPassword.setState(State.CORRECT)
            }
            is PasswordUiEvent.OnTextInvalid ->{
                binding.passProgressBar.isGone = true
                binding.cvPassword.setState(State.ERROR)
            }
            is PasswordUiEvent.OnProcess ->{
                binding.passProgressBar.isVisible = true
                binding.passProgressBar.progress = event.textSize
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleDaysEvents(event: DaysUiEvent){
        when (event){
            is DaysUiEvent.OnTextEmpty -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
            is DaysUiEvent.OnTextValid -> {
                if(binding.etDateDays.text.toString().length == 2){
                    binding.etDateMonthes.requestFocus()
                }
            }
            is DaysUiEvent.OnNotFocus -> {
                if(binding.etDateDays.text.toString().length == 1) {
                    binding.etDateDays.setText("0${binding.etDateDays.text}")
                }
            }
            is DaysUiEvent.OnTextInvalid -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
        }
    }

    private fun handleMonthesEvents(event: MonthesUiEvent){
        when (event){
            is MonthesUiEvent.OnTextEmpty -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
            is MonthesUiEvent.OnTextValid -> {
                if(binding.etDateMonthes.text.toString().length == 2){
                    binding.etDateYears.requestFocus()
                }
            }
            is MonthesUiEvent.OnNotFocus -> {
                if(binding.etDateMonthes.text.toString().length == 1) {
                    binding.etDateMonthes.setText("0${binding.etDateMonthes.text}")
                }
            }
            is MonthesUiEvent.OnTextInvalid -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
        }
    }
}