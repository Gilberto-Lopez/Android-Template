package com.example.androidtemplate.data

import com.example.androidtemplate.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The log repository.
 *
 * This class is main-safe, meaning functions in this class are safe to call from the main thread.
 * Since this class interacts with the database through [LogDao], doing possibly long-running
 * blocking operations, it's in charge of moving execution off the main thread using [withContext].
 *
 * See
 * [Use coroutines for main-safety](https://developer.android.com/kotlin/coroutines#use-coroutines-for-main-safety)
 * and
 * [Suspend functions should be safe to call from the main thread](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe)
 * for more information.
 *
 * This class (data layer) exposes suspend functions.
 *
 * See
 * [The data and business layer should expose suspend functions and Flows](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#coroutines-data-layer)
 * for more information.
 */
@Singleton
class LogRepository @Inject constructor(
    private val logDao: LogDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /** Add a new entry with the given [message]. */
    suspend fun addLog(message: String) {
        withContext(ioDispatcher) {
            logDao.insert(Log(message, System.currentTimeMillis()))
        }
    }

    /**
     * Retrieves all log entries.
     * @return The list of log entries.
     */
    suspend fun getAll(): List<Log> {
        return withContext(ioDispatcher) {
            logDao.getAll()
        }
    }

    /** Deletes all log entries. */
    suspend fun deleteAll() {
        withContext(ioDispatcher) {
            logDao.deleteAll()
        }
    }
}
