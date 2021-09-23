package com.midina.registration_ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.midina.core_ui.ui.State
import android.view.inputmethod.EditorInfo

import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.midina.registration_ui.databinding.RegistrationFragmentBinding

//TODO Make ProgressBar Beauty
//TODO Make CustomView correct size( test on physical device)

class RegistrationFragment: Fragment() {

    private lateinit var binding: RegistrationFragmentBinding
    private lateinit var fAuth: FirebaseAuth
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

        fAuth = Firebase.auth

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModel.firstNameEvents.observe(viewLifecycleOwner,{handleFirstNameEvents(it)})
        viewModel.lastNameEvents.observe(viewLifecycleOwner, {handleLastNameEvents(it)})
        viewModel.emailEvents.observe(viewLifecycleOwner,{handleEmailEvents(it)})
        viewModel.passwordEvents.observe(viewLifecycleOwner,{handlePasswordEvents(it)})
        viewModel.daysEvents.observe(viewLifecycleOwner,{handleDaysEvents(it)})
        viewModel.monthesEvents.observe(viewLifecycleOwner,{handleMonthesEvents(it)})
        viewModel.yearsEvents.observe(viewLifecycleOwner,{handleYearsEvents(it)})

        binding.btRegist.setOnClickListener {
            viewModel.registrationIsClicked()
            //todo navigate to draft fragment
        }

        //todo move to VM
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


        binding.cbMale.setOnClickListener{
            MaleChecked()
        }

        binding.cbFemale.setOnClickListener{
            FemaleChecked()
        }

        binding.cbUnspecified.setOnClickListener{
            UnspecifiedChecked()
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
                binding.tvPassRequirements.isVisible = true
                binding.cvPassword.setState(State.ERROR)
            }
            is PasswordUiEvent.OnTextValid -> {
                binding.tvPassRequirements.isGone = true
                binding.cvPassword.setState(State.CORRECT)
            }
            is PasswordUiEvent.OnTextInvalid ->{
                binding.passProgressBar.isGone = true
                binding.tvPassRequirements.isVisible = true
                binding.cvPassword.setState(State.ERROR)
            }
            is PasswordUiEvent.OnProcess ->{
                binding.passProgressBar.isVisible = true
                binding.passProgressBar.progress = event.textSize
                binding.cvPassword.setState(State.DEFAULT)
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
                binding.cvDate.setState(viewModel.dateState)
                binding.etDateMonthes.requestFocus()
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
                    binding.cvDate.setState(viewModel.dateState)

                    binding.etDateYears.requestFocus()
                }
            }
            is MonthesUiEvent.OnNotFocus -> {
                if(binding.etDateMonthes.text.toString().length == 1) {
                //TODO Unsubscribe
                    //todo передать textWatcher (note OnNotFocus doesnt use TextWatcher)
                    //binding.etDateMonthes.removeTextChangedListener()
                    binding.etDateMonthes.setText("0${binding.etDateMonthes.text}")
                //TODO Subscribe
                }
            }
            is MonthesUiEvent.OnTextInvalid -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
        }
    }

    private fun handleYearsEvents(event: YearsUiEvent){
        when (event){
            is YearsUiEvent.OnTextEmpty -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
            is YearsUiEvent.OnTextValid -> {
                binding.cvDate.setState(viewModel.dateState)
            }
            is YearsUiEvent.OnNotFocus -> {

            }
            is YearsUiEvent.OnTextInvalid -> {
                binding.tvDateRq.isVisible = true
                binding.cvDate.setState(State.ERROR)
            }
        }
    }
    private fun MaleChecked(){
        binding.cbMale.isClickable = false

        binding.cbFemale.isClickable = true
        binding.cbFemale.isChecked = false

        binding.cbUnspecified.isClickable = true
        binding.cbUnspecified.isChecked = false

        binding.cvGender.setState(State.CORRECT)
        viewModel.genderState = State.CORRECT
    }

    private fun FemaleChecked(){
        binding.cbFemale.isClickable = false

        binding.cbMale.isClickable = true
        binding.cbMale.isChecked = false

        binding.cbUnspecified.isClickable = true
        binding.cbUnspecified.isChecked = false
        binding.cvGender.setState(State.CORRECT)
        viewModel.genderState = State.CORRECT
    }

    private fun UnspecifiedChecked(){
        binding.cbUnspecified.isClickable = false

        binding.cbMale.isClickable = true
        binding.cbMale.isChecked = false

        binding.cbFemale.isClickable = true
        binding.cbFemale.isChecked = false
        binding.cvGender.setState(State.CORRECT)
        viewModel.genderState = State.CORRECT
    }
}