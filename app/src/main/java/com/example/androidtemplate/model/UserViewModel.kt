package com.example.androidtemplate.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.androidtemplate.data.Result
import com.example.androidtemplate.data.User

/**
 * This view model stores the user data and login status.
 *
 * This view model is shared between fragments.
 */
class UserViewModel : ViewModel() {

    // The encapsulated user data, exposed as a LiveData object
    private var _user: MutableLiveData<User> = MutableLiveData(null)
    val user: LiveData<User> get() = _user

    // User login status, exposed through calls to login()
    private val login = Transformations.map(user) { user: User? ->
        user?.let { Result.success(it) } ?: Result.failure("Auth failed")
    }

    /**
     * Mock user authentication. The user with email `test@example.com` and password `pass` can
     * login.
     * @param email The user email.
     * @param password The user password.
     * @return A [LiveData] object containing a [Result] with the user data if
     * authentication was successful.
     */
    fun login(email: String, password: String): LiveData<Result<User>> {
        if (email == "test@example.com" && password == "pass") {
            _user.value = User(email)
        } else {
            _user.value = null
        }
        return login
    }

}
