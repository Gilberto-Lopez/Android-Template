package com.example.androidtemplate.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.androidtemplate.R
import com.example.androidtemplate.data.User
import com.example.androidtemplate.databinding.FragmentAccessGrantedBinding
import com.example.androidtemplate.model.UserViewModel

/**
 * This class implements a basic example of conditional navigation.
 *
 * User must login to access this screen.
 *
 * When reaching this destination, if the user is not logged in, the user will be redirected to the
 * login screen [LoginFragment] to authenticate.
 *
 * * If authentication is successful, a welcome message will be shown.
 * * If user chose not to authenticate, user will be redirected to main screen [MainFragment].
 *
 * See
 * [Conditional navigation](https://developer.android.com/guide/navigation/navigation-conditional)
 * for more information.
 */
class AccessGrantedFragment : Fragment() {

    // View binding
    private var _binding: FragmentAccessGrantedBinding? = null
    private val binding get() = _binding!!

    // ViewModel shared between fragments in this activity (MainActivity)
    // See activityViewModels() documentation
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()

        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        // Observe LOGIN_SUCCESSFUL
        // When user returns from LoginFragment, if the user did not log in redirect back to
        // MainFragment (the start destination in the navigation graph)
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry) { success ->
                if (!success) {
                    Log.d(TAG, "User did not log in: Returning to MainFragment")

                    val startDestination = navController.graph.startDestination
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccessGrantedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Enter onViewCreated()")

        // User data is exposed via LiveData
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.d(TAG, "User logged in")

                // Show content in this fragment that is only accessible when user is logged in
                showWelcomeMessage(user)
            } else {
                Log.d(TAG, "Redirecting to login screen")

                // Redirect to login screen (LoginFragment) if there's no user data
                findNavController().navigate(R.id.login_fragment)
            }
        }
    }

    /**
     * Shows a welcome message to [user].
     * @param user The user that just logged in.
     */
    private fun showWelcomeMessage(user: User) {
        binding.userEmail.text = getString(R.string.welcome_user, user.email)
    }

    companion object {
        private val TAG = AccessGrantedFragment::class.java.simpleName
    }
}
