package com.sandbox.myhal.viewmodels

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignUpViewModel : BaseAuthentication() {


    var firstName = ""
    set(value) {
        field = value.trim{
            it < ' '
        }
        validateForm()
    }

    fun validateForm(): Boolean {

        return when{
            TextUtils.isEmpty(firstName) -> {
               false
            }
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

