package com.midina.login_ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.midina.core_ui.ui.BaseFragment
import com.midina.core_ui.ui.OnBottomNavHideListener
import com.midina.login_ui.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect

const val TAG = "LoginFragment"

class LoginFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_login

    private var binding: FragmentLoginBinding? = null

    private var resultGoogleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Toast.makeText(context, "${result.resultCode}", Toast.LENGTH_LONG).show()
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    viewModel.googleSignInClicked(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
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

      //  (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title)

        binding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )

        binding?.lifecycleOwner = viewLifecycleOwner

        binding?.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.loginEvents
                .collect {
                    handleLoginEvents(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.resetEvents
                .collect {
                    handleLResetEvents(it)
                }
        }

        binding?.tvRegister?.setOnClickListener {
            findNavController().navigate(R.id.action_registration_navigation)
        }

        binding?.btLogin?.setOnClickListener {
            viewModel.signInClicked()
        }

        binding?.ivGoogle?.setOnClickListener {

            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val mGoogleSignInClient =
                this.activity?.let { activity -> GoogleSignIn.getClient(activity, gso) }

            val signInIntent: Intent = mGoogleSignInClient!!.signInIntent

            resultGoogleLauncher.launch(signInIntent)
        }

        val callbackManager = CallbackManager.Factory.create()
        binding?.btFacebookSignIn?.setPermissions("email")
        binding?.btFacebookSignIn?.fragment = this
        binding?.btFacebookSignIn?.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d(TAG, "button onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "button onError")
                }

                override fun onSuccess(result: LoginResult) {
                    viewModel.facebookSignInClicked(result.accessToken.token)
                }
            })

        binding?.ivFacebook?.setOnClickListener {
            binding?.btFacebookSignIn?.callOnClick()
        }

        binding?.btFacebookSignIn?.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                callbackManager,
                listOf("public_profile", "email")
            )
        }

        binding?.tietEmail?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val colour = context?.let { ContextCompat.getColor(it, R.color.active_colour) }
                if (colour != null) {
                    binding?.tvEmail?.setTextColor(colour)
                }
            } else {
                viewModel.emailFocusChanged()
                val colour = context?.let { ContextCompat.getColor(it, R.color.static_colour) }
                if (colour != null) {
                    binding?.tvEmail?.setTextColor(colour)
                }
            }
        }

        binding?.tietPassword?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val colour = context?.let { ContextCompat.getColor(it, R.color.active_colour) }
                if (colour != null) {
                    binding?.tvPassword?.setTextColor(colour)
                }
            } else {
                val colour = context?.let { ContextCompat.getColor(it, R.color.static_colour) }
                if (colour != null) {
                    binding?.tvPassword?.setTextColor(colour)
                }
            }
        }

        binding?.tvForgot?.setOnClickListener {
            viewModel.forgotPasswordClicked()
        }

        return binding?.root
    }

    private fun handleLoginEvents(event: LoginEvent) {
        if (event is LoginEvent.OnSuccess) {
            findNavController().navigate(R.id.action_draft_navigation, null)
        } else if (event is LoginEvent.OnError) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLResetEvents(event: PasswordResetEvent) {
        if (event == PasswordResetEvent.OnError) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        } else if (event == PasswordResetEvent.OnSuccess) {
            Toast.makeText(context, "Check email to reset the pass", Toast.LENGTH_SHORT).show()
        }
    }
}