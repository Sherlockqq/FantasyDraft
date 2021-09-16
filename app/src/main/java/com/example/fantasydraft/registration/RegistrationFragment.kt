package com.example.fantasydraft.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.fantasydraft.R
import com.example.fantasydraft.databinding.RegistrationFragmentBinding
import com.example.fantasydraft.utils.State
import java.lang.NumberFormatException

//TODO Correct Date input
//TODO Check everything is put and correctly
//TODO Correct showing CustomView
//TODO Make ProgressBar Beauty

class RegistrationFragment: Fragment() {

    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registration_fragment,
            container,
            false)


        binding.etName.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if(!b && binding.etName.text.toString().isEmpty()) {
                binding.tvNameRequest.isVisible = true
                binding.cvFirstName.setState(State.ERROR)
            }
        }

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.tvNameRequest.isGone = true
                binding.cvFirstName.setState(State.CORRECT)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }

        })


        binding.etLastName.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if(!b && binding.etLastName.text.toString().isEmpty()) {
                binding.tvLastNameRequest.isVisible = true
                binding.cvLastname.setState(State.ERROR)
            }
        }
        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.tvLastNameRequest.isGone = true
                binding.cvLastname.setState(State.CORRECT)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }

        })

        binding.etEmail.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if(!b && !isEmailValid(binding.etEmail.text)) {
                binding.tvEmailRequest.isVisible = true
                binding.cvEmail.setState(State.ERROR)
            }
        }

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty()){
                    binding.tvEmailRequest.isVisible = true
                    binding.cvEmail.setState(State.ERROR)
                }
                if(isEmailValid(s)){
                    binding.tvEmailRequest.isGone = true
                    binding.cvEmail.setState(State.CORRECT)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }

        })

        binding.etPassword.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if(!b && binding.etPassword.text.toString().length < 8) {
                binding.tvPassRequirements.isVisible = true
                binding.cvPassword.setState(State.ERROR)
            }
        }

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if(s.toString().isEmpty()) {
                    binding.passProgressBar.isGone = true
                    binding.cvPassword.setState(State.ERROR)
                }
                else {
                    val textSize = s.toString().length
                    if(textSize == 8){
                        binding.tvPassRequirements.isGone = true
                        binding.cvPassword.setState(State.CORRECT)
                    }
                    binding.passProgressBar.isVisible = true
                    binding.passProgressBar.progress = textSize
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }

        })

        binding.etDateDays.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s?.length == 2){
                    binding.etDateMonthes.requestFocus()
                }
                val daysNum: Int
                try{
                   daysNum  = s.toString().toInt()
                    if(daysNum in 1..31){
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
        }

        binding.cbFemale.setOnClickListener{
            binding.cbFemale.isClickable = false

            binding.cbMale.isClickable = true
            binding.cbMale.isChecked = false

            binding.cbUnspecified.isClickable = true
            binding.cbUnspecified.isChecked = false
            binding.cvGender.setState(State.CORRECT)
        }

        binding.cbUnspecified.setOnClickListener{
            binding.cbUnspecified.isClickable = false

            binding.cbMale.isClickable = true
            binding.cbMale.isChecked = false

            binding.cbFemale.isClickable = true
            binding.cbFemale.isChecked = false
            binding.cvGender.setState(State.CORRECT)
        }

        return binding.root
    }
    private fun isEmailValid(text:Editable?) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
    }
}