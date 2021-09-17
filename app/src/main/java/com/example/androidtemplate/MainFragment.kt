package com.example.androidtemplate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidtemplate.databinding.FragmentMainBinding

/**
 * The main fragment. This is the start destination as defined in the `nav_graph`, that is, the base
 * destination at the bottom of the back stack.
 *
 * See
 * [Designate a screen as the start destination](https://developer.android.com/guide/navigation/navigation-getting-started#Designate-start)
 * for more information.
 */
class MainFragment : Fragment() {

    // An instance of the binding class
    // Property only valid between onCreateView() and onDestroyView()
    // See https://developer.android.com/topic/libraries/view-binding#fragments for more information
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // and keep the reference to the instance of the binding class
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the reference _binding
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Enter onViewCreated()")

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
