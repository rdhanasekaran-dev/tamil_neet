package com.dogood.tamilneet.testFile

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.dogood.tamilneet.R
import com.dogood.tamilneet.test.TestResultActivity
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_physics_select.*

class PhysicsSelectActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    var num_of_questions=""
    private lateinit var psradiogroup:RadioGroup
    private lateinit var radio:RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physics_select)
        val start=findViewById(R.id.start) as Button



        MobileAds.initialize(this,"ca-app-pub-5918143946387420/6673971056")
        val phsadview=findViewById(R.id.phsadView) as AdView
        val adRequest= AdRequest.Builder().build()
        phsadview.loadAd(adRequest)

        psradiogroup=findViewById(R.id.ps_radio_group)
        psradiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            radio=findViewById(checkedId)
            num_of_questions=radio.text.toString()
        })

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-5918143946387420/5881147025"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                val i = Intent(applicationContext,CommonTestActivity::class.java)
                i.putExtra("subject","physics")
                i.putExtra("num",num_of_questions)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {

                super.onAdLoaded()

                val i = Intent(applicationContext,CommonTestActivity::class.java)
                i.putExtra("subject","physics")
                i.putExtra("num",num_of_questions)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }


        start.setOnClickListener {
            if(num_of_questions.equals("")){
                Toast.makeText(applicationContext,"Choose number of questions",Toast.LENGTH_SHORT).show()
            }else {
                    showAd()

            }
        }
    }

    private fun showAd(){
        if(mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }else{
            val i = Intent(applicationContext,CommonTestActivity::class.java)
            i.putExtra("subject","physics")
            i.putExtra("num",num_of_questions)
            startActivity(i)
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }
}