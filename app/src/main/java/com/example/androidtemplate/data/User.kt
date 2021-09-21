package com.example.androidtemplate.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * User data. Holds user email, password and handle.
 * @param email User email.
 * @param password User password.
 * @param userHandle User handle.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["user_handle"], unique = true)]
)
data class User(
    @PrimaryKey
    val email: String,

    val password: String,

    // The user handle is unique
    // Room only allows uniqueness by means of indices
    // See https://developer.android.com/training/data-storage/room/defining-data#column-indexing
    // for more information
    @ColumnInfo(name = "user_handle")
    val userHandle: String
)
