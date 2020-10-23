package com.sandbox.myhal.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.firebase.auth.FirebaseAuth
import com.sandbox.myhal.R
import com.sandbox.myhal.models.User
import com.sandbox.myhal.repository.CustomerCatalog
import com.sandbox.myhal.repository.CustomerFactory
import com.sandbox.myhal.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : BaseActivity() {


    lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        et_name.doAfterTextChanged { text -> viewModel.firstName = text.toString() ?: "" }
        et_email.doAfterTextChanged { text -> viewModel.emailAddress = text.toString() ?: "" }
        et_password.doAfterTextChanged { text -> viewModel.password = text.toString() ?: "" }

        btn_sign_up.setOnClickListener{
            registerUser()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }

    }

    private fun registerUser() {

        val mCustomerRepository = CustomerFactory.create()
        val mCustomerCatalog = CustomerCatalog(mCustomerRepository)

        val user = User( name=viewModel.firstName, email=viewModel.emailAddress)

        if (viewModel.validateForm()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            mCustomerCatalog.registerUser(this, user, viewModel.password)

        } else {
            showErrorSnackBar("There was an input error")
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(
            this,
            "you have successfully registered",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        finish()
    }

    fun userRegisteredUnSuccessful(message: String){
        hideProgressDialog()
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()

    }
}