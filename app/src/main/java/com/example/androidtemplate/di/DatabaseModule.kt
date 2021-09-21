package com.example.androidtemplate.di

import android.content.Context
import com.example.androidtemplate.data.AppDatabase
import com.example.androidtemplate.data.LogDao
import com.example.androidtemplate.data.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt [Module] to provide the database and data access objects.
 *
 * Since Hilt cannot constructor-inject instances of Room classes nor instances of interfaces, we
 * have to tell Hilt how to provide instances using methods annotated with [Provides].
 *
 * See
 * [Inject instances with @Provides](https://developer.android.com/training/dependency-injection/hilt-android#inject-provides)
 * for more information.
 *
 * The database is installed in the app's component, [SingletonComponent], that corresponds to the
 * app's lifecycle.
 *
 * See
 * [Component lifetimes](https://developer.android.com/training/dependency-injection/hilt-android#component-lifetimes)
 * for more information.
 */
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    /**
     * Provides the singleton database instance. This hilt binding is scoped to the app's component.
     */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    /**
     * Provides [UserDao] instance.
     */
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    /**
     * Provides [LogDao] instance.
     */
    @Provides
    fun provideLogDao(appDatabase: AppDatabase): LogDao {
        return appDatabase.logDao()
    }
}
