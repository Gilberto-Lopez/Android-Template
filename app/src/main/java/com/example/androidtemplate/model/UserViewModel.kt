package com.example.androidtemplate.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtemplate.data.Result
import com.example.androidtemplate.data.User
import com.example.androidtemplate.data.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * This view model stores the user data and login status.
 *
 * This view model is shared between fragments.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    // User login status, exposed through calls to login()
    private val login = MutableLiveData<Result<User>>(null)

    /** The user data, exposed as a [LiveData] object. */
    val user: LiveData<User> = Transformations.map(login) { result ->
        result?.let { if (result.isSuccess) result.value!! else null }
    }

    /**
     * User authentication.
     * @param email The user email.
     * @param password The user password.
     * @return A [LiveData] object containing a [Result] with the user data if
     * authentication was successful.
     */
    fun login(email: String, password: String): LiveData<Result<User>> {
        // Fetching users in the database might block the UI thread
        // Launch a coroutine on a background thread for database operations
        // See https://developer.android.com/kotlin/coroutines#use-coroutines-for-main-safety
        // for more information
        // The ViewModel should prefer creating coroutines and expose immutable observable types
        // instead of exposing suspend functions to views
        // See  https://developer.android.com/kotlin/coroutines/coroutines-best-practices#viewmodel-coroutines
        // for more information
        login.value = Result.loading()
        viewModelScope.launch {
            val result = loginSuspend(email, password)
            login.value = result
        }
        return login
    }

    // TODO: Move this function to repository
    private suspend fun loginSuspend(email: String, password: String): Result<User> {
        // Suspend functions must be safe to call from the main thread
        // Classes doing long-running blocking operations in a coroutine are in charge of moving
        // execution off the main thread using withContext
        // See https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe
        // for more information
        return withContext(Dispatchers.IO) {
            val user = userDao.get(email)
            return@withContext when {
                user == null -> Result.failure("User not found")
                user.password != password -> Result.failure("Incorrect password")
                else -> Result.success(user)
            }
        }
    }
}
