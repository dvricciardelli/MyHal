package com.sandbox.myhal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.sandbox.myhal.R
import com.sandbox.myhal.models.User
import com.sandbox.myhal.repository.CustomerCatalog
import com.sandbox.myhal.repository.DataFactory
import com.sandbox.myhal.viewmodels.SignInViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : BaseActivity() {

    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

       viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        et_email_sign_in.doAfterTextChanged { text -> viewModel.emailAddress = text.toString() ?: "" }
        et_password_sign_in.doAfterTextChanged { text -> viewModel.password = text.toString() ?: "" }

        btn_sign_in.setOnClickListener{
            signInRegisteredUser()
        }

        setupActionBar()
    }

    override fun receiveUserData(user: User){
        hideProgressDialog()
        startActivity(Intent(this, IntroductionActivity::class.java))
        finish()

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_in_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun signInRegisteredUser(){

        if(viewModel.validateForm()){
            showProgressDialog(resources.getString(R.string.please_wait))

            val mCustomerRepository = DataFactory.createCustomer()
            val mCustomerCatalog = CustomerCatalog(mCustomerRepository)

            val user = User( email = viewModel.emailAddress)
            mCustomerCatalog.signInUser(this, user, viewModel.password)

        }
    }

    fun signInUnSuccessful(message: String){
        hideProgressDialog()
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()

    }

}