package com.example.androidtemplate.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
 * This view model stores sign in status and navigation event.
 *
 * This view model is _not shared between fragments.
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    // Sign in status, exposed through calls to signIn()
    private val signIn = MutableLiveData<Result<User>>(null)

    // Encapsulated navigation event
    private val _eventSignInFinish = MutableLiveData(false)
    /** `true` when registration was successful and navigation must occur, `false` otherwise.  */
    val eventSignInFinish: LiveData<Boolean> get() = _eventSignInFinish

    /** Signal navigation out of sign in screen is done. */
    fun onSignInFinishComplete() {
        _eventSignInFinish.value = false
    }

    /**
     * New user registration.
     * @param email The user email.
     * @param password The user password.
     * @param handle The user handle.
     * @return A [LiveData] object containing a [Result] with the user data if
     * registration was successful.
     */
    fun signIn(email: String, password: String, handle: String): LiveData<Result<User>> {
        signIn.value = Result.loading()
        viewModelScope.launch {
            val result = registerUser(email, password, handle)
            signIn.value = result
            if (result.isSuccess) {
                _eventSignInFinish.value = true
            }
        }
        return signIn
    }

    // TODO: Move this function to repository
    private suspend fun registerUser(
        email: String,
        password: String,
        handle: String
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            val dbUser = userDao.get(email)
            if (dbUser != null) {
                // A user with the given email already exists, cannot register new user
                return@withContext Result.failure("Email already exists")
            }
            val dbUserHandle = userDao.getByHandle(handle)
            if (dbUserHandle != null) {
                // A user with the given handle already exists, cannot register new user
                return@withContext Result.failure("Handle already in use")
            }
            // Register new user into the database
            val newUser = User(email, password, handle)
            userDao.insert(newUser)
            return@withContext Result.success(newUser)
        }
    }
}
