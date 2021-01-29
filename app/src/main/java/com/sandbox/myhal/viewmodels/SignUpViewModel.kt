package com.sandbox.myhal.viewmodels

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : BaseAuthentication() {

    private val _isFormValid = MutableLiveData<Boolean>()

    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

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

