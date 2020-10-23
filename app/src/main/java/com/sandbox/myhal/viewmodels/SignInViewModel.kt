package com.sandbox.myhal.viewmodels

import android.text.TextUtils
import androidx.lifecycle.ViewModel

class SignInViewModel: BaseAuthentication() {

    fun validateForm(): Boolean {

        return when{

            TextUtils.isEmpty(emailAddress)-> {
                false
            }
            TextUtils.isEmpty(password)-> {
                false
            } else ->  {
                true
            }
        }
    }
}