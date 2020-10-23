package com.sandbox.myhal.viewmodels

import androidx.lifecycle.ViewModel

open class BaseAuthentication : ViewModel(){

    var emailAddress = ""
        set(value) {
            field = value.trim{
                it < ' '
            }
        }
    var password = ""
        set(value) {
            field = value.trim{
                it < ' '
            }
        }

}