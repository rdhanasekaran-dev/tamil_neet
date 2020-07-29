package com.dogood.tamilneet.importantLinks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dogood.tamilneet.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_links_subject.*

class LinksSubjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_links_subject)

        var link_type=intent.getStringExtra("link_type")

        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val lsadview=findViewById(R.id.lssmadView) as AdView
        val lsadrequest= AdRequest.Builder().build()
        lsadview.loadAd(lsadrequest)

        phyMatlin.setOnClickListener {
            val i= Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","physicsMaterial")
            startActivity(i)
        }

        cheMatlin.setOnClickListener {
            val i=Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","chemistryMaterial")
            startActivity(i)
        }

        botMatlin.setOnClickListener {
            val i=Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","botonyMaterial")
            startActivity(i)
        }

        zooMatlin.setOnClickListener {
            val i=Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","zoologyMaterial")
            startActivity(i)
        }

    }
}