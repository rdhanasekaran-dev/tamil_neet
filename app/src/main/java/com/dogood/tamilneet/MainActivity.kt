package com.dogood.tamilneet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.ads.MobileAds
import java.util.logging.LogManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713")

        Handler().postDelayed({
            val intent = Intent(this,HomeActivity::class.java)
            finish()
            startActivity(intent)
        },2000)
    }
}