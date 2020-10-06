package com.sandbox.myhal.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.location.Location
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sandbox.myhal.R
import com.sandbox.myhal.repository.FirestoreCustomerRepository
import com.sandbox.myhal.models.PlayerModel
import com.sandbox.myhal.utils.Constants
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    private lateinit var mFusedLocationClient : FusedLocationProviderClient
    private lateinit var mSharedPreferences: SharedPreferences
    private var mPlayerDetails: PlayerModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name.typeface = typeFace

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            requestNewLocationData()
                            Handler().postDelayed({

                                val playerDetail = Gson().toJson(mPlayerDetails)
                                val editor = mSharedPreferences.edit()
                                editor.putString(Constants.PLAYER_POSITION_DATA, playerDetail)
                                editor.apply()

                                var currentUserId = FirestoreCustomerRepository().getCurrentUserId()

                                if(!currentUserId.isNotEmpty()){
                                    startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                                } else {
                                    startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                                }

                                finish()

                            }, 2500)
                        }

                        if(report.isAnyPermissionPermanentlyDenied){
                            Toast.makeText(
                                this@SplashActivity,
                                "You have denied location permission. Please add all location permissions",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        if(!report.areAllPermissionsGranted()){
                            showRationalDialogForPermissions()

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        //showRationalDialogForPermissions()
                        token!!.continuePermissionRequest()

                    }
                }).onSameThread()
                .check()

    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000
        mLocationRequest.numUpdates = 1

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallBack,
            Looper.myLooper())

    }

    private val mLocationCallBack = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            val mLastLocation: Location = locationResult!!.lastLocation
            val latitude = mLastLocation.latitude
            Log.i("Current Latitude", "$latitude")
            val longitude = mLastLocation.longitude
            Log.i("Current Longitude", "$longitude")
            mPlayerDetails = PlayerModel(
                latitude,
                longitude
            )

        }

    }

    private fun showRationalDialogForPermissions() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.data = uri
                    startActivity(intent)
                    finish()
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause() called")
    }

    companion object {

        const val EXTRA_PLAYER_DETAILS = "extra_player_detail"
        private const val PERMISSION_LOCATION = 1
    }

}