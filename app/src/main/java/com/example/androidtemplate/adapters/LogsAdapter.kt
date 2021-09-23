package com.example.androidtemplate.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtemplate.data.Log
import com.example.androidtemplate.databinding.ListItemLogBinding
import com.example.androidtemplate.utils.DateFormatter

/**
 * This [RecyclerView.Adapter] holds [Log] entries.
 *
 * See
 * [Implementing your adapter and view holder](https://developer.android.com/guide/topics/ui/layout/recyclerview#implement-adapter)
 * for more information.
 *
 * @param dataset The list of log entries.
 * @param dateFormatter Formatter for log timestamps.
 */
class LogsAdapter(
    dataset: List<Log> = emptyList(),
    private val dateFormatter: DateFormatter = DateFormatter()
) : RecyclerView.Adapter<LogsAdapter.LogViewHolder>() {

    /** The list of [Log]s to display on screen. */
    var dataset: List<Log> = dataset
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = dataset[position]
        holder.bind(log, dateFormatter)
    }

    override fun getItemCount(): Int = dataset.size

    /** [RecyclerView.ViewHolder] for [Log]s. */
    class LogViewHolder private constructor(private val binding: ListItemLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds log data to the view
         * @param log The log.
         * @param dateFormatter Formatter for log timestamps.
         */
        fun bind(log: Log, dateFormatter: DateFormatter) {
            binding.logMessage.text = log.message
            binding.logTimestamp.text = dateFormatter.formatDate(log.timestamp)
        }

        companion object {
            /** Returns a new [LogViewHolder] instance from the given [parent] view group. */
            fun from(parent: ViewGroup): LogViewHolder {
                return LogViewHolder(
                    ListItemLogBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}
