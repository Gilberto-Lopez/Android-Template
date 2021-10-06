package com.example.androidtemplate.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.androidtemplate.R
import com.example.androidtemplate.data.User
import com.example.androidtemplate.databinding.FragmentAccessGrantedBinding
import com.example.androidtemplate.model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

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
@AndroidEntryPoint
class AccessGrantedFragment : Fragment() {

    // View binding
    private var _binding: FragmentAccessGrantedBinding? = null
    private val binding get() = _binding!!

    // ViewModel shared between fragments in this activity (MainActivity)
    // See activityViewModels() documentation
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccessGrantedBinding.inflate(inflater, container, false)

        // The following code, previously found in onCreate(), would cause the following error:
        //
        // IllegalStateException: You cannot access the NavBackStackEntry's SavedStateHandle until
        //      it is added to the NavController's back stack (i.e., the Lifecycle of the
        //      NavBackStackEntry reaches the CREATED state).
        //
        // The navController is ready AFTER onCreate()
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

                    val startDestination = navController.graph.startDestinationId
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
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
        binding.userEmail.text = getString(R.string.welcome_user, user.userHandle)
    }

    companion object {
        private val TAG = AccessGrantedFragment::class.java.simpleName
    }
}
