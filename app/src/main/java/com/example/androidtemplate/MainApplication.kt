package com.example.androidtemplate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The application class. All apps that use Hilt must have an [Application] class annotated with
 * [HiltAndroidApp] to trigger Hilt's code generation.
 *
 * See
 * [Hilt application class](https://developer.android.com/training/dependency-injection/hilt-android#application-class)
 * for more information.
 */
@HiltAndroidApp
class MainApplication : Application()
