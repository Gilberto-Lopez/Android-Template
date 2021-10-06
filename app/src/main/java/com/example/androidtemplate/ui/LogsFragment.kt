package com.example.androidtemplate.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtemplate.R
import com.example.androidtemplate.adapters.LogsAdapter
import com.example.androidtemplate.data.Logger
import com.example.androidtemplate.databinding.FragmentLogsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * This fragment displays log data using [RecyclerView].
 *
 * See
 * [Create dynamic lists with RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
 * for more information.
 */
@AndroidEntryPoint
class LogsFragment : Fragment() {

    // View binding
    private var _binding: FragmentLogsBinding? = null
    private val binding get() = _binding!!

    // Event logger
    @Inject lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logs_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_log_menu_button -> {
                // Clear log
                logger.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Enter onViewCreated()")

        // Empty adapter
        // Prevents getting warning "No adapter attached; skipping layout"
        val logsAdapter = LogsAdapter()

        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = logsAdapter
        }

        // Update dataset once logger gets the list from the database
        // If logsAdapter were to be created inside this observer's body once logs is not null,
        // recyclerView would have no adapter attached when drawn, sending warning "No adapter
        // attached; skipping layout"
        logger.getAll().observe(viewLifecycleOwner) { logs ->
            logs?.let { logsAdapter.dataset = it }
        }
    }

    companion object {
        private val TAG = LogsFragment::class.java.simpleName
    }
}
