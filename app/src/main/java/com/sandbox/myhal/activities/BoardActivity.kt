package com.sandbox.myhal.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sandbox.myhal.R
import com.sandbox.myhal.adapters.BoardItemsAdapter
import com.sandbox.myhal.models.Board
import com.sandbox.myhal.models.User
import com.sandbox.myhal.repository.BoardCatalog
import com.sandbox.myhal.repository.CustomerCatalog
import com.sandbox.myhal.repository.DataFactory
import com.sandbox.myhal.utils.Constants
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_board.drawer_layout
import kotlinx.android.synthetic.main.board_list.*


class BoardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        val mCustomerRepository = DataFactory.createCustomer()
        val mCustomerCatalog = CustomerCatalog(mCustomerRepository)

        showProgressDialog(resources.getString(R.string.please_wait))
        mCustomerCatalog.loadUserData(this@BoardActivity)


        fab_create_board.setOnClickListener {
            val intent = Intent(this@BoardActivity, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserDetails.name)
            intent.putExtra(Constants.CUSTOMER_ID, mUserDetails.id)
            // TODO (Step 2: Here now pass the unique code for StartActivityForResult.)
            // START
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
            // END
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this,
                    MyProfileActivity::class.java),
                    MainActivity.MY_PROFILE_REQUEST_CODE
                )
            }
            R.id.nav_sign_out -> {
                val mCustomerRepository = DataFactory.createCustomer()
                val mCustomerCatalog = CustomerCatalog(mCustomerRepository)
                mCustomerCatalog.signOut()
                val intent = Intent(this, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            R.id.nav_board -> {
                val intent = Intent(this, BoardActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_map -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE
        ) {
            // Get the user updated details.
            val mCustomerRepository = DataFactory.createCustomer()
            val mCustomerCatalog = CustomerCatalog(mCustomerRepository)

            mCustomerCatalog.loadUserData(this@BoardActivity)
        }
        // TODO (Step 4: Here if the result is OK get the updated boards list.)
        // START
        else if (resultCode == Activity.RESULT_OK
            && requestCode == CREATE_BOARD_REQUEST_CODE
        ) {

            val mBoardRepository = DataFactory.createBoard()
            val mBoardCatalog = BoardCatalog(mBoardRepository)
            mBoardCatalog.getBoardsList(this@BoardActivity, mUserDetails.id)
            // Get the latest boards list.

        }
        // END
        else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun receiveUserData(user: User) {
        // TODO (Step 7: Initialize the user details variable)
        // START
        // Initialize the user details variable
        hideProgressDialog()
        mUserDetails = user
        // END
        // The instance of the header view of the navigation view.
        val headerView = nav_view.getHeaderView(0)

        // The instance of the user image of the navigation view.
        val navUserImage = headerView.findViewById<ImageView>(R.id.iv_user_image)

        // Load the user image in the ImageView.
        Glide
            .with(this@BoardActivity)
            .load(user.image) // URL of the image
            .centerCrop() // Scale type of the image.
            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
            .into(navUserImage) // the view in which the image will be loaded.

        // The instance of the user name TextView of the navigation view.
        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        // Set the user name
        navUsername.text = user.name

        showProgressDialog(resources.getString(R.string.please_wait))
        val mBoardRepository = DataFactory.createBoard()
        val mBoardCatalog = BoardCatalog(mBoardRepository)
        mBoardCatalog.getBoardsList(this@BoardActivity, mUserDetails.id)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            // A double back press function is added in Base Activity.
            doubleBackToExit()
        }
    }

    fun populateBoardsListToUI(boardsList: ArrayList<Board>){
        hideProgressDialog()

        if (boardsList.size > 0) {

            rv_boards_list.visibility = View.VISIBLE
            tv_no_boards_available.visibility = View.GONE

            rv_boards_list.layoutManager = LinearLayoutManager(this@BoardActivity)
            rv_boards_list.setHasFixedSize(true)

            // Create an instance of BoardItemsAdapter and pass the boardList to it.
            val adapter = BoardItemsAdapter(this@BoardActivity, boardsList)
            rv_boards_list.adapter = adapter // Attach the adapter to the recyclerView.

            // TODO (Step 9: Add click event for boards item and launch the TaskListActivity)
            // START
            adapter.setOnClickListener(object :
                BoardItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@BoardActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })
            // END
        } else {
            rv_boards_list.visibility = View.GONE
            tv_no_boards_available.visibility = View.VISIBLE
        }
    }



    companion object {
        //A unique code for starting the activity for result
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        // TODO (Step 1: Add a unique code for starting the create board activity for result)
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }
}