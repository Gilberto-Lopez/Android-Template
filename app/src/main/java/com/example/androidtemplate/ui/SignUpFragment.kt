package com.example.androidtemplate.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.androidtemplate.R
import com.example.androidtemplate.data.Logger
import com.example.androidtemplate.data.Result
import com.example.androidtemplate.data.Status
import com.example.androidtemplate.data.User
import com.example.androidtemplate.databinding.FragmentSignUpBinding
import com.example.androidtemplate.model.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Sign up screen.
 *
 * Can register a new user with email, password and user handle. If user with given email or
 * handle already exists, a [Snackbar] notifying the failure will appear on screen. If registration
 * was successful we navigate back to [MainFragment].
 *
 * Navigation in this screen is handled with events, observing [SignUpViewModel.eventSignUpFinish]
 * to determine when navigation mush occur and calling [SignUpViewModel.onSignUpFinishComplete] when
 * done.
 */
@AndroidEntryPoint
class SignUpFragment : Fragment() {

    // View binding
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    // View model not shared between fragments in this activity (MainActivity)
    // See viewModels() documentation
    private val signUpViewModel: SignUpViewModel by viewModels()

    // Event logger
    @Inject lateinit var logger: Logger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        // If registration is successful, hasFinished is true
        // Navigate up, back to MainFragment
        signUpViewModel.eventSignUpFinish.observe(viewLifecycleOwner) { hasFinished ->
            if (hasFinished) {
                findNavController().navigateUp()
                signUpViewModel.onSignUpFinishComplete()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Enter onViewCreated()")

        (requireActivity() as AppCompatActivity).let {
            it.findViewById<Toolbar>(R.id.activity_toolbar)?.run {
                title = getString(R.string.sign_up)
                setNavigationIcon(R.drawable.ic_back)
                setNavigationOnClickListener { findNavController().navigateUp() }
            }
        }

        // When user clicks the sign up button, retrieve user data and attempt register new user
        binding.signUpButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val handle = binding.handle.text.toString()

            Log.d(TAG, "SIGN UP button clicked: email=$email, password=$password, handle=$handle")

            signUp(email, password, handle)
        }
    }

    /**
     * [Observer] to be used in calls to [signUp].
     * Kept in a variable to avoid multiple observers being created on each signUp call.
     */
    private val signUpObserver = Observer<Result<User>> { result ->
        when (result.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                Log.d(TAG, "Registration successful")

                logger.addLog("User @${result.value!!.userHandle} has been registered")
            }
            Status.FAILURE -> {
                Log.d(TAG, "Registration failed")

                logger.addLog("Failed attempt to register new user: ${result.message!!}")

                showErrorMessage(result.message)
            }
        }
    }

    /**
     * Pass [email], [password] and [handle] to the view model for registration.
     *
     * Upon successful registration, app navigates back to [MainFragment].
     *
     * Upon failed registration the user is notified.
     * @param email User's email.
     * @param password User's password.
     * @param handle User's handle.
     */
    private fun signUp(email: String, password: String, handle: String) {
        signUpViewModel.signUp(email, password, handle).observe(viewLifecycleOwner, signUpObserver)
    }

    /**
     * Show a [Snackbar] notifying the user that authentication failed.
     * @param message The message explaining the error.
     */
    private fun showErrorMessage(message: String) {
        Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    companion object {
        private val TAG = SignUpFragment::class.java.simpleName
    }
}
