package com.dogood.tamilneet

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.dogood.tamilneet.model.WebviewData
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity(),RewardedVideoAdListener {

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    var webviewlist:MutableList<WebviewData> = mutableListOf()
    var valueEn=""
    var typeEn=""
    lateinit var progressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111")
        val wvadview = findViewById(R.id.wvadView) as AdView
        val adrequest = AdRequest.Builder().build()
        wvadview.loadAd(adrequest)

        progressBar=findViewById(R.id.progressBar)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                super.onAdClosed()
                val i=Intent(applicationContext,parent.javaClass)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }


        }

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        mRewardedVideoAd=MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener=this
        loadRewardedVideoAd()


        valueEn = intent.getStringExtra("value")
        val webview = findViewById(R.id.webview) as WebView

        add()
        download.setOnClickListener {
            if(mRewardedVideoAd.isLoaded()){
                mRewardedVideoAd.show()
            }else{
                Toast.makeText(applicationContext,"Video is not ready. Try few seconds later",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun add(){
        val ref= FirebaseDatabase.getInstance().getReference("/Webview/webview")
        var name:String

        webviewlist= mutableListOf()

        val dataListener=object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(WebviewData::class.java)

                for(shot: DataSnapshot in snapshot.children){
                    if (data != null) {
                        shot.getValue(WebviewData::class.java)?.let { webviewlist.add(it) }
                    }
                }
                addData()
            }
        }
        ref.addValueEventListener(dataListener)
    }

    private fun addData(){
        for(i in webviewlist){
                if(i.value.equals(valueEn)){
                    typeEn=i.type
                    webview.setInitialScale(1)
                    webview.settings.javaScriptEnabled = true
                    webview.settings.loadWithOverviewMode = true
                    webview.settings.useWideViewPort = true
                    webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
                    webview.isScrollbarFadingEnabled = true
                    webview.settings.setSupportZoom(true)
                    webview.settings.builtInZoomControls = true
                    webview.settings.displayZoomControls = false
                    webview.loadUrl(i.link)

                    webview.webViewClient=object :android.webkit.WebViewClient(){
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            progressBar.visibility= View.GONE
                            webview.visibility= VISIBLE
                        }
                    }
                }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        showAd()
    }

    private fun showAd(){
        if(mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }else{
            val i=Intent(applicationContext,HomeActivity::class.java)
            startActivity(i)
        }
    }

    private fun loadRewardedVideoAd(){
        if(!mRewardedVideoAd.isLoaded){
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", AdRequest.Builder().build())
        }
    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
    }

    override fun onRewardedVideoAdLeftApplication() {
        TODO("Not yet implemented")
    }

    override fun onRewardedVideoAdLoaded() {
    }

    override fun onRewardedVideoAdOpened() {
        TODO("Not yet implemented")
    }

    override fun onRewardedVideoCompleted() {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(typeEn))
        startActivity(i)
    }

    override fun onRewarded(p0: RewardItem?) {
        TODO("Not yet implemented")
    }

    override fun onRewardedVideoStarted() {
        TODO("Not yet implemented")
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        mRewardedVideoAd.pause(this)
        super.onPause()
    }

    override fun onResume() {
        mRewardedVideoAd.resume(this)
        super.onResume()
    }

    override fun onDestroy() {
        mRewardedVideoAd.destroy(this)
        super.onDestroy()
    }

}