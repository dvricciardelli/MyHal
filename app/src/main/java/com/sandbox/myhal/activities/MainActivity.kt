package com.sandbox.myhal.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.sandbox.myhal.utils.Constants
import com.sandbox.myhal.R
import com.sandbox.myhal.models.PlayerModel
import com.sandbox.myhal.models.User
import com.sandbox.myhal.repository.CustomerCatalog
import com.sandbox.myhal.repository.DataFactory
import com.sandbox.weatherapp.models.WeatherResponse
import com.sandbox.weatherapp.network.WeatherService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit.*

class MainActivity : BaseActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    companion object{
        const val MY_PROFILE_REQUEST_CODE: Int = 1
    }

    private var mPlayerDetails: PlayerModel? = null
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val playerLocationJsonString = mSharedPreferences.getString(Constants.PLAYER_POSITION_DATA, "")

        val mCustomerRepository = DataFactory.createCustomer()
        val mCustomerCatalog = CustomerCatalog(mCustomerRepository)
        mCustomerCatalog.loadUserData(this)

        if(!playerLocationJsonString.isNullOrEmpty()){
            mPlayerDetails = Gson().fromJson(playerLocationJsonString, PlayerModel::class.java)
        }

        if(mPlayerDetails != null){

            val supportMapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }

        fabViewWeather.setOnClickListener{
            val intent = Intent(this@MainActivity, WeatherActivity::class.java)
            intent.putExtra("latitude", mPlayerDetails!!.latitude)
            intent.putExtra("longitude", mPlayerDetails!!.longitude)
            startActivity(intent)

        }

        fabViewProfile.setOnClickListener{
            toggleDrawer()
        }

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val position = LatLng(mPlayerDetails!!.latitude, mPlayerDetails!!.longitude)
        googleMap!!.addMarker(MarkerOptions().position(position))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)
    }

    fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
    }

    private fun toggleDrawer(){
        if(drawer_layout.isDrawerOpen((GravityCompat.START))){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen((GravityCompat.START))){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE){

            val mCustomerRepository = DataFactory.createCustomer()
            val mCustomerCatalog = CustomerCatalog(mCustomerRepository)
            mCustomerCatalog.loadUserData(this)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this@MainActivity,
                    MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
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
                val intent = Intent(this@MainActivity, BoardActivity::class.java)
                startActivity(intent)

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun receiveUserData(user: User){
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_user_image)
        tv_username.text = user.name
        mUser = user
    }

}