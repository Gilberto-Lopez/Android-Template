package com.example.androidtemplate.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.androidtemplate.data.Result
import com.example.androidtemplate.data.Status
import com.example.androidtemplate.data.User
import com.example.androidtemplate.databinding.FragmentLoginBinding
import com.example.androidtemplate.model.UserViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * The login screen. (Conditional navigation example.)
 *
 * User is redirected to this screen from [AccessGrantedFragment] if not logged in.
 *
 * * If authentication is successful, user will be redirected back to [AccessGrantedFragment].
 * * If authentication fails, a [Snackbar] is shown on screen.
 * * If user chooses not to authenticate by clicking the back button, user will be redirected back
 *   to [AccessGrantedFragment].
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    // View binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // View model shared between fragments in this activity (MainActivity)
    // See activityViewModels() documentation
    private val userViewModel: UserViewModel by activityViewModels()

    // The previous destination's SavedStateHandle
    // Here we set the value whether or not the user logged in successfully
    private lateinit var savedStateHandle: SavedStateHandle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Enter onViewCreated()")

        // The initial value of LOGIN_SUCCESSFUL is false, the user has not logged in
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)

        // When user clicks the login button, retrieve email and password and authenticate
        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            Log.d(TAG, "LOGIN button clicked: email=$email, password=$password")

            login(email, password)
        }
    }

    /**
     * [Observer] to be used in calls to [login].
     * Kept in a variable to avoid multiple observers being created on each login call.
     */
    private val loginObserver = Observer<Result<User>> { result ->
        when (result.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                Log.d(TAG, "Successful Authentication")

                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                findNavController().popBackStack()
            }
            Status.FAILURE -> {
                Log.d(TAG, "Authentication Failed")

                showErrorMessage(result.message!!)
            }
        }
    }

    /**
     * Pass [email] and [password] to the view model for authentication.
     *
     * Upon successful authentication sets [LOGIN_SUCCESSFUL] to true and this fragment pops itself
     * out of the back stack.
     *
     * Upon failed authentication the user is notified.
     * @param email User's email.
     * @param password User's password.
     */
    private fun login(email: String, password: String) {
        userViewModel.login(email, password).observe(viewLifecycleOwner, loginObserver)
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
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
        private val TAG = LoginFragment::class.java.simpleName
    }
}
