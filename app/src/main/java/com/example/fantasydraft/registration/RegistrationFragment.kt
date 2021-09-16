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
import java.lang.NumberFormatException

//TODO Asc About Date Input
//TODO Correct Date input
//TODO Check everything is put and correctly
//TODO Correct showing CustomView
//TODO Make ProgressBar Beauty

//TODO Show layout
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


        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()){
                    //TODO GREEN CustomView
                    binding.tvNameRequest.isGone = true
                    binding.cvFirstName.setState()
                }else{
                    //TODO RED CustomView
                    binding.tvNameRequest.isVisible = true
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }
        })

        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()){
                    //TODO GREEN CustomView
                    binding.tvLastNameRequest.isGone = true
                }else{
                    //TODO RED CustomView
                    binding.tvLastNameRequest.isVisible = true
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }
        })

//        binding.etEmail.setOnKeyListener(object : View.OnKeyListener {
//            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
//                // if the event is a key down event on the enter button
//                if (event.action == KeyEvent.ACTION_DOWN &&
//                    keyCode == KeyEvent.KEYCODE_ENTER
//                ) {
//                    if(isEmailValid(binding.etEmail.text)){
//                        //TODO GREEN CustomView
//                        binding.tvEmailRequest.isGone = true
//                    }else{
//                        //TODO RED CustomView
//
//                        binding.tvEmailRequest.isVisible = true
//                    }
//
//                    return true
//                }
//                return false
//            }
//            fun isEmailValid(text:Editable?) : Boolean{
//                return android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
//            }
//        })


        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(isEmailValid(s)){
                    //TODO GREEN CustomView
                    binding.tvEmailRequest.isGone = true
                }else{
                    //TODO RED CustomView
                    //TODO Not to show when user is not finished
                    binding.tvEmailRequest.isVisible = true

                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }
            fun isEmailValid(text:Editable?) : Boolean{
                return android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if(s.toString().isEmpty()) {
                    binding.passProgressBar.isGone = true
                    //TODO RED CustomView
                }
                else {
                    val textSize = s.toString().length
                    binding.passProgressBar.isVisible = true
                    binding.passProgressBar.progress = textSize
                    if(textSize < 8){
                        binding.tvPassRequirements.isVisible = true
                        //TODO RED CustomView
                        //TODO Not to show when user is not finished
                    }
                    else{
                        binding.tvPassRequirements.isGone = true
                        //TODO GREEN CustomView

                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.i("FSDF","DSFS")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("FSDF","DSFS")
            }

        })

//        TODO request focus
      //  binding.etDateDays.setOnFocusChangeListener()
        binding.etDateDays.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
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
        }

        binding.cbFemale.setOnClickListener{
            binding.cbFemale.isClickable = false

            binding.cbMale.isClickable = true
            binding.cbMale.isChecked = false

            binding.cbUnspecified.isClickable = true
            binding.cbUnspecified.isChecked = false
        }

        binding.cbUnspecified.setOnClickListener{
            binding.cbUnspecified.isClickable = false

            binding.cbMale.isClickable = true
            binding.cbMale.isChecked = false

            binding.cbFemale.isClickable = true
            binding.cbFemale.isChecked = false
        }

        return binding.root
    }

}