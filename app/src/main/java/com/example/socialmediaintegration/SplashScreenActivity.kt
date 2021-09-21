package com.example.socialmediaintegration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imageView2.alpha = 0f
        imageView2.animate().setDuration(1500).alpha(1f).withEndAction {

            val intent = Intent(this, MainActivity::class.java)

            startActivities(arrayOf(intent))

            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)

            finish()

        }
    }
}