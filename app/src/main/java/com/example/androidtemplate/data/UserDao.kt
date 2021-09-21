package com.example.androidtemplate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data access object. Provides methods that the app uses to interact with data in the database.
 *
 * See
 * [Data access object (DAO)](https://developer.android.com/training/data-storage/room#dao)
 * and
 * [Accessing data using Room DAOs](https://developer.android.com/training/data-storage/room/accessing-data)
 * for more information.
 */
@Dao
interface UserDao {

    /**
     * Inserts a new [user] into the database if it doesn't exist.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: User)

    /**
     * Updates the given [user] in the database.
     */
    @Update
    fun update(user: User)

    /**
     * Retrieves user registered with the given [email].
     */
    @Query("SELECT * FROM users WHERE email = :email")
    fun get(email: String): User?

    /**
     * Retrieves user registered with the given [handle].
     */
    @Query("SELECT * FROM users WHERE user_handle = :handle")
    fun getByHandle(handle: String): User?

    /**
     * Retrieves all users from the database.
     */
    @Query("SELECT * FROM users")
    fun getAll(): List<User>
}
