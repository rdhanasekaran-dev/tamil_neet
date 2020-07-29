package com.dogood.tamilneet.testFile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.dogood.tamilneet.R
import com.google.android.gms.ads.*

class ChemistrySelectActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd

    var num_of_questions=""
    private lateinit var csradiogroup: RadioGroup
    private lateinit var radio: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chemistry_select)
        val start=findViewById(R.id.start) as Button



        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val chsadview=findViewById(R.id.chsadView) as AdView
        val adRequest= AdRequest.Builder().build()
        chsadview.loadAd(adRequest)

        csradiogroup=findViewById(R.id.cs_radio_group)
        csradiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            radio=findViewById(checkedId)
            num_of_questions=radio.text.toString()
        })

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                val i = Intent(applicationContext,CommonTestActivity::class.java)
                i.putExtra("subject","chemistry")
                i.putExtra("num",num_of_questions)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        start.setOnClickListener {
            if(num_of_questions.equals("")){
                Toast.makeText(applicationContext,"Choose number of questions", Toast.LENGTH_SHORT).show()
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
            i.putExtra("subject","chemistry")
            startActivity(i)
        }
    }
}