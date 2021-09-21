package com.example.androidtemplate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The database. This class defines the database configuration.
 *
 * See
 * [Save data in a local database using Room](https://developer.android.com/training/data-storage/room)
 * for more information.
 */
@Database(entities = [User::class, Log::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // This class is annotated with @Database and an array of entities,
    // This class must extend RoomDatabase,
    // For each Dao class, an abstract method with zero arguments and return type the Dao class is
    // needed
    // See https://developer.android.com/training/data-storage/room#database for more information

    /**
     * Returns an instance of the Dao [UserDao]
     */
    abstract fun userDao(): UserDao

    /**
     * Returns an instance of the Dao [LogDao]
     */
    abstract fun logDao(): LogDao

    companion object {
        // Maintain a single instance of the database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the database.
         */
        fun getInstance(context: Context): AppDatabase {
            // Adapted from Android Sunflower app (https://github.com/android/sunflower)
            // See https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
            // for more information
            var instance = INSTANCE
            return instance ?: synchronized(this) {
                instance = INSTANCE
                return instance ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Builds the database
        // Adapted from Android Sunflower app (https://github.com/android/sunflower)
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "android_template_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
        }
    }
}
