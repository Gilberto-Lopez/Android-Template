package com.example.androidtemplate.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidtemplate.di.MainDispatcher
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Logger provides functionality to add, retrieve and delete log entries. This class is scoped to
 * the activity component.
 *
 * This class contains its own [CoroutineScope] to keep track of coroutines created inside this
 * class's functions. Coroutines are needed to call suspend functions in [LogRepository].
 *
 * See
 * [CoroutineScope](https://developer.android.com/kotlin/coroutines/coroutines-adv#coroutinescope)
 * for more information.
 *
 * This class exposes observable data.
 *
 * See
 * [The data and business layer should expose suspend functions and Flows](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#coroutines-data-layer)
 * for more information.
 */
@ActivityScoped
class Logger @Inject constructor(
    private val logRepository: LogRepository,
    @MainDispatcher mainDispatcher: CoroutineDispatcher
) {

    // Scope for coroutines created in this class
    private val scope = CoroutineScope(Job() + mainDispatcher)

    /** Add a new entry with the given [message]. */
    fun addLog(message: String) {
        scope.launch {
            logRepository.addLog(message)
        }
    }

    /**
     * Retrieves all log entries.
     * @return The list of log entries as [LiveData].
     */
     fun getAll(): LiveData<List<Log>> {
        val logs = MutableLiveData<List<Log>>(null)
        scope.launch {
            logs.value = logRepository.getAll()
        }
        return logs
    }

    /** Deletes all log entries. */
    fun deleteAll() {
        scope.launch {
            logRepository.deleteAll()
        }
    }
}
