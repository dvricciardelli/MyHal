package com.sandbox.myhal.repository

import com.sandbox.myhal.activities.SignInActivity
import com.sandbox.myhal.activities.SignUpActivity
import com.sandbox.myhal.models.User

class CustomerCatalog(customerRepository: CustomerRepository) {

   val mCustomerRepository: CustomerRepository = customerRepository

    fun registerUser(activity: SignUpActivity, userInfo: User, password: String){
        mCustomerRepository.registerUser(activity, userInfo, password)
    }

    fun signInUser(activity: SignInActivity, userInfo: User, password: String){
        mCustomerRepository.signInUser(activity, userInfo, password)
    }

    fun isLoggedIn(): Boolean {
        return mCustomerRepository.isLoggedIn()

    }


}