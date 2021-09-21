package com.example.androidtemplate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.androidtemplate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Hilt can provide dependencies to Android classes annotated with [AndroidEntryPoint].
 *
 * See
 * [Inject dependencies into Android classes](https://developer.android.com/training/dependency-injection/hilt-android#android-classes)
 * for more information.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Instance of the binding class
    // See https://developer.android.com/topic/libraries/view-binding#activities for more information
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Enter onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
