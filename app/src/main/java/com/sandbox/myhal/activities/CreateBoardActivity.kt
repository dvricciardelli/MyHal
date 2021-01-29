package com.sandbox.myhal.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sandbox.myhal.R
import com.sandbox.myhal.databinding.ActivityCreateBoardBinding
import com.sandbox.myhal.databinding.ActivitySignUpBinding
import com.sandbox.myhal.models.Board
import com.sandbox.myhal.models.User
import com.sandbox.myhal.repository.BoardCatalog
import com.sandbox.myhal.repository.CustomerCatalog
import com.sandbox.myhal.repository.DataFactory
import com.sandbox.myhal.utils.Constants
import com.sandbox.myhal.viewmodels.BoardViewModel
import com.sandbox.myhal.viewmodels.FormViewModel
import com.sandbox.myhal.viewmodels.SignInViewModel
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.io.IOException
import java.lang.Exception

class CreateBoardActivity : BaseActivity() {
    private var mSelectedImageFileUri: Uri? = null

    // A global variable for Username
    lateinit private var mUserName: String
    lateinit private var mCustomerId: String

    // A global variable for a board image URL
    private var mBoardImageURL: String = ""

    lateinit var binding: ActivityCreateBoardBinding

    val viewModel: BoardViewModel by lazy {
        ViewModelProvider(this).get(BoardViewModel::class.java)
    }

    val mBoardRepository = DataFactory.createBoard()
    val mBoardCatalog = BoardCatalog(mBoardRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_board)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel


        if (intent.hasExtra(Constants.NAME) && intent.hasExtra(Constants.CUSTOMER_ID)) {
            mUserName = intent.getStringExtra(Constants.NAME)!!
            mCustomerId = intent.getStringExtra(Constants.CUSTOMER_ID)!!

        }

        iv_board_image.setOnClickListener { view ->

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this@CreateBoardActivity)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        btn_create.setOnClickListener {

            // Here if the image is not selected then update the other details of user.
            if (mSelectedImageFileUri != null) {
                uploadBoardImage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard(mBoardImageURL)
            }
        }
    }

    /**
     * This function will notify the user after tapping on allow or deny
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@CreateBoardActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            mSelectedImageFileUri = data.data

            try {
                // Load the board image in the ImageView.
                Glide
                    .with(this@CreateBoardActivity)
                    .load(Uri.parse(mSelectedImageFileUri.toString())) // URI of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                    .into(iv_board_image) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * A function to upload the Board Image to storage and getting the downloadable URL of the image.
     */
    private fun uploadBoardImage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {
            mBoardCatalog.uploadBoardImage(this@CreateBoardActivity, mSelectedImageFileUri)
        }
    }

    /**
     * A function to make an entry of a board in the database.
     */
    public fun createBoard(boardImageURL: String) {

        //  A list is created to add the assigned members.
        //  This can be modified later on as of now the user itself will be the member of the board.
        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(mCustomerId) // adding the current user id

        // Creating the instance of the Board and adding the values as per parameters.
        val board = Board(
            et_board_name.text.toString(),
            mBoardImageURL,
            mUserName,
            assignedUsersArrayList
        )
        mBoardCatalog.createBoard(this, board)
    }

    /**
     * A function for notifying the board is created successfully.
     */
    fun boardCreatedSuccessfully() {

        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun boardUpdateUnSuccessful(exception: Exception) {
        Log.e(
            "error",
            "Error while creating a board.",
            exception
        )
        hideProgressDialog()
        finish()
    }


}