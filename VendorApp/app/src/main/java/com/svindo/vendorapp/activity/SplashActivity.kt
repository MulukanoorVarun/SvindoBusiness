package com.svindo.vendorapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.svindo.vendorapp.R
import com.svindo.vendorapp.utils.SharedPreference
import com.svindo.vendorapp.utils.showToast


@SuppressLint("CustomSplashScreen", "Registered")
class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference = SharedPreference(this)

      //  sharedPreference.save("mobile_no","9390776532")

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit

        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is
    }
}