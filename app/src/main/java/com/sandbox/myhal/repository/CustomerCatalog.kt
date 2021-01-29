package com.sandbox.myhal.repository

import android.app.Activity
import android.net.Uri
import com.sandbox.myhal.activities.BaseActivity
import com.sandbox.myhal.activities.MyProfileActivity
import com.sandbox.myhal.activities.SignInActivity
import com.sandbox.myhal.activities.SignUpActivity
import com.sandbox.myhal.models.User

class CustomerCatalog(customerRepository: CustomerRepository): CustomerRepository {

   val mCustomerRepository: CustomerRepository = customerRepository

    override fun registerUser(activity: SignUpActivity, userInfo: User, password: String){
        mCustomerRepository.registerUser(activity, userInfo, password)
    }

    override fun loadUserData(activity: BaseActivity) {
        mCustomerRepository.loadUserData(activity)
    }

    override fun updateUserProfileData(
        activity: MyProfileActivity,
        userHashMap: HashMap<String, Any>
    ) {
        mCustomerRepository.updateUserProfileData(activity, userHashMap)
    }

    override fun uploadUserImage(activity: MyProfileActivity, fileUri: Uri?) {
        mCustomerRepository.uploadUserImage(activity, fileUri)
    }

    override fun signInUser(activity: SignInActivity, userInfo: User, password: String){
        mCustomerRepository.signInUser(activity, userInfo, password)
    }

    override fun isLoggedIn(): Boolean {
        return mCustomerRepository.isLoggedIn()

    }

    override fun signOut() {
        mCustomerRepository.signOut()
    }


}