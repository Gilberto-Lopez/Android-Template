package com.example.androidtemplate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * The log Dao.
 */
@Dao
interface LogDao {

    /**
     * Insert a new log entry.
     */
    @Insert
    fun insert(log: Log)

    /**
     * Clear all log entries.
     */
    @Query("DELETE FROM logs")
    fun deleteAll()

    /**
     * Retrieve all log entries.
     */
    @Query("SELECT * FROM logs")
    fun getAll(): List<Log>
}