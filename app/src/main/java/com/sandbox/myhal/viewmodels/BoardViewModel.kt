package com.sandbox.myhal.viewmodels

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel(){

    val boardName = MutableLiveData<String>("")


    val valid = MediatorLiveData<Boolean>().apply {

        addSource(boardName) {
            val valid = isFormValid()
            Log.i(boardName.value, valid.toString())
            value = valid
        }

    }

    fun isFormValid(): Boolean {

        return when{
            TextUtils.isEmpty(boardName.value) -> {
                false
            }

            else ->  {
                true
            }
        }

    }

}