package com.dogood.tamilneet.dailytest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dogood.tamilneet.R
import com.dogood.tamilneet.testFile.TodaysTestActivity
import com.google.android.gms.ads.*

class DailyTestIntroActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_test_intro)

        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val dtadview=findViewById(R.id.dtadView) as AdView
        val adrequest= AdRequest.Builder().build()
        dtadview.loadAd(adrequest)



        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                super.onAdClosed()
                val i=Intent(applicationContext,TodaysTestActivity::class.java)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        val start=findViewById(R.id.start) as Button
        start.setOnClickListener {
            showAd()
        }
    }

    private fun showAd(){
        if(mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }else{
            val i=Intent(applicationContext,TodaysTestActivity::class.java)
            startActivity(i)
        }
    }
}