package com.sandbox.myhal.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.sandbox.myhal.R
import com.sandbox.myhal.databinding.ActivitySignUpBinding
import com.sandbox.myhal.models.User
import com.sandbox.myhal.repository.CustomerCatalog
import com.sandbox.myhal.repository.DataFactory
import com.sandbox.myhal.viewmodels.FormViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : BaseActivity() {

   lateinit var binding: ActivitySignUpBinding

    val viewModel: FormViewModel by lazy {
        ViewModelProvider(this).get(FormViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_sign_up)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel



        //viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        //binding.setLifecycleOwner(this)
        // 3. Set the viewModel instance
        //binding.viewModel = viewModel
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        //tvName.doAfterTextChanged { text -> viewModel.firstName = text.toString() ?: "" }
        //tvEmail.doAfterTextChanged { text -> viewModel.emailAddress = text.toString() ?: "" }
        //tvPassword.doAfterTextChanged { text -> viewModel.password = text.toString() ?: "" }

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

        val mCustomerRepository = DataFactory.createCustomer() //gets my instance
        val mCustomerCatalog = CustomerCatalog(mCustomerRepository) //use the customerCatalog interface
        Log.i("Register", viewModel.emailAddress.value)

        val user = User( name=viewModel.firstName.value!!, email=viewModel.emailAddress.value!!)

        showProgressDialog(resources.getString(R.string.please_wait))
        mCustomerCatalog.registerUser(this, user, viewModel.password.value!!)

        /*if (viewModel.isFormValid()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            mCustomerCatalog.registerUser(this, user, viewModel.password)

        } else {
            showErrorSnackBar("There was an input error")
        }*/
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

//