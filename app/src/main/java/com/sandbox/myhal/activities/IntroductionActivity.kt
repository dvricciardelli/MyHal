package com.sandbox.myhal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sandbox.myhal.R
import com.sandbox.myhal.models.PlayerModel
import com.sandbox.myhal.models.Speech
import kotlinx.android.synthetic.main.activity_introduction.*

class IntroductionActivity : AppCompatActivity() {

    private var mPlayerDetails: PlayerModel? = null
    private var currentIndex = 0


    private val calloutBank = listOf(
        Speech(1, "This is my comment.  There are many like them but this one is mine"),
        Speech(2, "This is my comment after my comment")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        tv_callout.text = calloutBank[0].callout


        if(intent.hasExtra(SplashActivity.EXTRA_PLAYER_DETAILS)){
            mPlayerDetails =
                intent.getSerializableExtra(SplashActivity.EXTRA_PLAYER_DETAILS)
                        as PlayerModel

        }

        tv_callout.setOnClickListener{
            currentIndex = currentIndex + 1

            if ( currentIndex <= calloutBank.size - 1 ){

                tv_callout.text = calloutBank[currentIndex].callout
            } else {
                val intent = Intent(this@IntroductionActivity, MainActivity::class.java)
                intent.putExtra(SplashActivity.EXTRA_PLAYER_DETAILS, mPlayerDetails)
                startActivity(intent)
                finish()

            }

        }
    }
}