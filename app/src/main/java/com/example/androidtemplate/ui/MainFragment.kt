package com.example.androidtemplate.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidtemplate.R
import com.example.androidtemplate.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main fragment. This is the start destination as defined in the `nav_graph`, that is, the base
 * destination at the bottom of the back stack.
 *
 * See
 * [Designate a screen as the start destination](https://developer.android.com/guide/navigation/navigation-getting-started#Designate-start)
 * for more information.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    // An instance of the binding class
    // Property only valid between onCreateView() and onDestroyView()
    // See https://developer.android.com/topic/libraries/view-binding#fragments for more information
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This fragment is participating in the population of the options menu
        // See https://developer.android.com/guide/fragments/appbar#activity-register
        // for more information
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // and keep the reference to the instance of the binding class
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        // Set up the action bar (navigation icon, navigation listener, title, etc)
        // See https://developer.android.com/guide/fragments/appbar#nav-icon for more information
        (requireActivity() as AppCompatActivity).let {
            it.findViewById<Toolbar>(R.id.activity_toolbar)?.run {
                title = getString(R.string.app_name)
                navigationIcon = null
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the reference _binding
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate this fragment's menu
        // See https://developer.android.com/guide/fragments/appbar#activity-inflate
        // for more information
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu click events
        // See https://developer.android.com/guide/fragments/appbar#activity-click
        // for more information
        return when (item.itemId) {
            R.id.logs_menu_button -> {
                Log.d(TAG, "LOGS button clicked: Navigating to LogsFragment")

                requireView().findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToLogsFragment())
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Enter onViewCreated()")

        binding.registerButton.setOnClickListener {
            Log.d(TAG, "REGISTER button clicked: Navigating to SignInFragment")

            it.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToSigninFragment())
        }

        binding.accessButton.setOnClickListener {
            Log.d(TAG, "ACCESS button clicked: Navigating to AccessGrantedFragment")

            it.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToAccessGrantedFragment())
        }
    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}
