package com.sandbox.myhal.repository

import android.app.Activity
import android.net.Uri
import com.sandbox.myhal.activities.BaseActivity
import com.sandbox.myhal.activities.MyProfileActivity
import com.sandbox.myhal.activities.SignInActivity
import com.sandbox.myhal.activities.SignUpActivity
import com.sandbox.myhal.models.User

interface CustomerRepository {

    fun signInUser(activity: SignInActivity, userInfo: User, password: String)
    fun registerUser(activity: SignUpActivity, userInfo: User, password: String)
    fun loadUserData(activity: BaseActivity)
    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>)
    fun uploadUserImage(activity: MyProfileActivity, fileUri: Uri?)
    fun isLoggedIn(): Boolean
    fun signOut()

}