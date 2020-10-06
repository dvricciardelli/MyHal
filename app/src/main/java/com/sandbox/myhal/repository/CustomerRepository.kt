package com.sandbox.myhal.repository

import com.sandbox.myhal.activities.SignInActivity
import com.sandbox.myhal.activities.SignUpActivity
import com.sandbox.myhal.models.User

interface CustomerRepository {

    fun signInUser(activity: SignInActivity)
    fun registerUser(activity: SignUpActivity, userInfo: User, password: String)

}