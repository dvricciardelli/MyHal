package com.sandbox.myhal.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sandbox.myhal.Constants
import com.sandbox.myhal.R
import com.sandbox.myhal.models.PlayerModel
import com.sandbox.weatherapp.models.WeatherResponse
import com.sandbox.weatherapp.network.WeatherService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mPlayerDetails: PlayerModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.hasExtra(SplashActivity.EXTRA_PLAYER_DETAILS)){
            mPlayerDetails =
                intent.getSerializableExtra(SplashActivity.EXTRA_PLAYER_DETAILS)
                        as PlayerModel
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

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val position = LatLng(mPlayerDetails!!.latitude, mPlayerDetails!!.longitude)
        googleMap!!.addMarker(MarkerOptions().position(position))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)
    }

    private fun getLocationWeatherDetails(latitude: Double, longitude: Double){
        if(Constants.isNetworkAvailable(this)){
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service: WeatherService = retrofit
                .create<WeatherService>(WeatherService::class.java)

            val listCall: Call<WeatherResponse> = service.getWeather(
                latitude, longitude, Constants.METRIC_UNIT, Constants.APP_ID
            )

            listCall.enqueue(object : Callback<WeatherResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    response: Response<WeatherResponse>,
                    retrofit: Retrofit
                ) {

                    // Check weather the response is success or not.
                    if (response.isSuccess) {

                        /** The de-serialized response body of a successful response. */
                        val weatherList: WeatherResponse = response.body()
                        Log.i("Response Result", "$weatherList")
                    } else {
                        // If the response is not success then we check the response code.
                        val sc = response.code()
                        when (sc) {
                            400 -> {
                                Log.e("Error 400", "Bad Request")
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                            }
                            else -> {
                                Log.e("Error", "Generic Error")
                            }
                        }
                    }
                }

                override fun onFailure(t: Throwable) {
                    Log.e("Errorrrrr", t.message.toString())
                }
            })

        } else {
            Toast.makeText(
                this@MainActivity,
                "No internet connection available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}