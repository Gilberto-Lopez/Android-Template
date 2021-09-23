package com.example.androidtemplate.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Qualifier

// Qualifiers

/** Provides [Dispatchers.Main]. */
@Qualifier annotation class MainDispatcher

/** Provides [Dispatchers.IO]. */
@Qualifier annotation class IODispatcher

/** Provides [Dispatchers.Unconfined]. */
@Qualifier annotation class UnconfinedDispatcher

/** Provides [Dispatchers.Default]. */
@Qualifier annotation class DefaultDispatcher

/**
 * Hilt [Module] to provide coroutine dispatchers.
 *
 * Dispatchers should be injected into classes that create new coroutines or call [withContext].
 *
 * See
 * [Inject Dispatchers](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#inject-dispatchers)
 * for more information.
 *
 * This module provides multiple dispatcher bindings. Since all dispatchers have type
 * [CoroutineDispatcher], each binding needs to be annotated with a qualifier to distinguish
 * which dispatcher is to be injected.
 *
 * See
 * [Provide multiple bindings for the same type](https://developer.android.com/training/dependency-injection/hilt-android#multiple-bindings)
 * for more information.
 */
@InstallIn(SingletonComponent::class)
@Module
class CoroutinesModule {
    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @IODispatcher
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @UnconfinedDispatcher
    @Provides
    fun provideUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
