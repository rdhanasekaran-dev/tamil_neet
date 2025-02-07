package com.dogood.tamilneet.importantLinks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.dogood.tamilneet.R
import com.dogood.tamilneet.syllabus.BiologySyllabus
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.links_layout_file.view.*
import kotlinx.android.synthetic.main.syllabus_layout_file.view.*
import java.util.ArrayList

class ImportantLinksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_important_links)


        MobileAds.initialize(this,"ca-app-pub-5918143946387420/8632978037")
        val il1adview=findViewById(R.id.il1adView) as AdView
        val ad1request= AdRequest.Builder().build()
        il1adview.loadAd(ad1request)

        val il2adview=findViewById(R.id.il2adView) as AdView
        val ad2request= AdRequest.Builder().build()
        il2adview.loadAd(ad2request)

        val il3adview=findViewById(R.id.il3adView) as AdView
        val ad3request= AdRequest.Builder().build()
        il3adview.loadAd(ad3request)

        val il4adview=findViewById(R.id.il4adView) as AdView
        val ad4request= AdRequest.Builder().build()
        il4adview.loadAd(ad4request)

        val material_links=findViewById(R.id.l_material_links) as Button
        material_links.setOnClickListener {
            val i=Intent(applicationContext,LinksSubjectActivity::class.java)
            i.putExtra("link_type","material")
            startActivity(i)
        }

        val question_paper_links=findViewById(R.id.l_QuestionPaper_links) as Button
        question_paper_links.setOnClickListener {
            val i=Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","questionPaper")
            startActivity(i)
        }

        val analysis_links=findViewById(R.id.l_question_analyze_links) as Button
        analysis_links.setOnClickListener {
            val i=Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","analysis")
            startActivity(i)
        }

        val video_links=findViewById(R.id.l_video_links) as Button
        video_links.setOnClickListener {
            val i=Intent(applicationContext,LinksShowActivity::class.java)
            i.putExtra("link_type","video")
            startActivity(i)
        }

    }
}