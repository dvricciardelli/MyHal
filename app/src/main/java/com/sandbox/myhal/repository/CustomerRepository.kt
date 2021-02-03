package com.sandbox.myhal.repository

import android.app.Activity
import android.net.Uri
import com.sandbox.myhal.activities.*
import com.sandbox.myhal.models.User

interface CustomerRepository {

    fun signInUser(activity: SignInActivity, userInfo: User, password: String)
    fun registerUser(activity: SignUpActivity, userInfo: User, password: String)
    fun loadUserData(activity: BaseActivity)
    fun updateUserProfileData(activity: BaseActivity, userHashMap: HashMap<String, Any>)
    fun uploadUserImage(activity: MyProfileActivity, fileUri: Uri?)
    fun getAssignedMembersListDetails(activity: Activity, assignedTo: ArrayList<String>)
    fun isLoggedIn(): Boolean
    fun getMemberDetails(activity: MembersActivity, email: String)
    fun getCurrentUserId(): String

    fun signOut()

}