package com.sandbox.myhal.repository

import android.app.Activity
import android.net.Uri
import com.sandbox.myhal.activities.*
import com.sandbox.myhal.models.Board
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
        activity: BaseActivity,
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

    override fun getMemberDetails(activity: MembersActivity, email: String) {
        return mCustomerRepository.getMemberDetails(activity, email)
    }

    override fun getAssignedMembersListDetails(
        activity: Activity,
        assignedTo: ArrayList<String>
    ) {
        mCustomerRepository.getAssignedMembersListDetails(activity, assignedTo)
    }

    override fun getCurrentUserId(): String {
        return mCustomerRepository.getCurrentUserId()
    }

    override fun signOut() {
        mCustomerRepository.signOut()
    }

}