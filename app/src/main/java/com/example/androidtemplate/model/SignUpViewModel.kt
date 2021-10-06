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
 * This view model stores sign up status and navigation event.
 *
 * This view model is _not shared between fragments.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    // Sign up status, exposed through calls to signUp()
    private val signUp = MutableLiveData<Result<User>>(null)

    // Encapsulated navigation event
    private val _eventSignUpFinish = MutableLiveData(false)
    /** `true` when registration was successful and navigation must occur, `false` otherwise.  */
    val eventSignUpFinish: LiveData<Boolean> get() = _eventSignUpFinish

    /** Signal navigation out of sign up screen is done. */
    fun onSignUpFinishComplete() {
        _eventSignUpFinish.value = false
    }

    /**
     * New user registration.
     * @param email The user email.
     * @param password The user password.
     * @param handle The user handle.
     * @return A [LiveData] object containing a [Result] with the user data if
     * registration was successful.
     */
    fun signUp(email: String, password: String, handle: String): LiveData<Result<User>> {
        signUp.value = Result.loading()
        viewModelScope.launch {
            val result = registerUser(email, password, handle)
            signUp.value = result
            if (result.isSuccess) {
                _eventSignUpFinish.value = true
            }
        }
        return signUp
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
                return@withContext Result.failure("Email $email already exists")
            }
            val dbUserHandle = userDao.getByHandle(handle)
            if (dbUserHandle != null) {
                // A user with the given handle already exists, cannot register new user
                return@withContext Result.failure("Handle @$handle already in use")
            }
            // Register new user into the database
            val newUser = User(email, password, handle)
            userDao.insert(newUser)
            return@withContext Result.success(newUser)
        }
    }
}
