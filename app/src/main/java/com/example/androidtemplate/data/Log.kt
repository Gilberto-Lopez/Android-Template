package com.example.androidtemplate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A log registering user interactions with the application.
 * @param message The message explaining the interaction.
 * @param timestamp The interaction's timestamp.
 */
@Entity(tableName = "logs")
data class Log(
    val message: String,
    val timestamp: Long
) {
    /** A unique id to identify log entries. */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
