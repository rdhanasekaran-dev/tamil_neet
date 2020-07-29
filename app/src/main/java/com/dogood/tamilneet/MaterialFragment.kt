package com.dogood.tamilneet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.dogood.tamilneet.dailytest.DailyTestIntroActivity
import com.dogood.tamilneet.importantLinks.ImportantLinksActivity
import com.dogood.tamilneet.practice.PracticeActivity
import com.dogood.tamilneet.syllabus.BiologySyllabus
import com.dogood.tamilneet.syllabus.ChemistrySyllabus
import com.dogood.tamilneet.syllabus.PhysicsSyllabus
import com.dogood.tamilneet.testFile.BiologySeletActivity
import com.dogood.tamilneet.testFile.ChemistrySelectActivity
import com.dogood.tamilneet.testFile.PhysicsSelectActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_material.*

class MaterialFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_material, container, false)


        MobileAds.initialize(context,"ca-app-pub-3940256099942544/6300978111")
        val madview=view.findViewById(R.id.madView) as AdView
        val adRequest=AdRequest.Builder().build()
        madview.loadAd(adRequest)

        MobileAds.initialize(context,"ca-app-pub-3940256099942544/6300978111")
        val madview1=view.findViewById(R.id.madView1) as AdView
        val adRequest1=AdRequest.Builder().build()
        madview1.loadAd(adRequest1)


        val phyMat =view.findViewById(R.id.phyMat) as RelativeLayout
        phyMat.setOnClickListener {
            val i=Intent(activity,PhysicsMaterial::class.java)
            startActivity(i)
        }
        val phySyl =view.findViewById(R.id.physyl) as RelativeLayout
        phySyl.setOnClickListener {
            val i=Intent(activity,PhysicsSyllabus::class.java)
            startActivity(i)
        }
        val phyTes =view.findViewById(R.id.phyTes) as Button
        phyTes.setOnClickListener {
            val i=Intent(activity,PhysicsSelectActivity::class.java)
            startActivity(i)
        }
        val phyPrac =view.findViewById(R.id.phyPrac) as Button
        phyPrac.setOnClickListener {
            val i=Intent(activity,PracticeActivity::class.java)
            i.putExtra("subject","physics")
            startActivity(i)
        }
        val cheMat =view.findViewById(R.id.cheMat) as RelativeLayout
        cheMat.setOnClickListener {
            val i=Intent(activity,ChemistryMaterial::class.java)
            startActivity(i)
        }
        val cheSyl =view.findViewById(R.id.chesyl) as RelativeLayout
        cheSyl.setOnClickListener {
            val i=Intent(activity,ChemistrySyllabus::class.java)
            startActivity(i)
        }
        val cheTes =view.findViewById(R.id.cheTes) as Button
        cheTes.setOnClickListener {
            val i=Intent(activity,ChemistrySelectActivity::class.java)
            startActivity(i)
        }
        val chePrac =view.findViewById(R.id.chePrac) as Button
         chePrac.setOnClickListener {
            val i=Intent(activity,PracticeActivity::class.java)
            i.putExtra("subject","chemistry")
            startActivity(i)
        }
        val bioMat =view.findViewById(R.id.bioMat) as RelativeLayout
        bioMat.setOnClickListener {
            val i=Intent(activity,BiologyMaterial::class.java)
            startActivity(i)
        }
        val bioSyl =view.findViewById(R.id.biosyl) as RelativeLayout
        bioSyl.setOnClickListener {
            val i=Intent(activity,BiologySyllabus::class.java)
            startActivity(i)
        }
        val bioTes =view.findViewById(R.id.bioTes) as Button
        bioTes.setOnClickListener {
            val i=Intent(activity,BiologySeletActivity::class.java)
            startActivity(i)
        }
        val bioPrac =view.findViewById(R.id.bioPrac) as Button
        bioPrac.setOnClickListener {
            val i=Intent(activity,PracticeActivity::class.java)
            i.putExtra("subject","biology")
            startActivity(i)
        }

        val write =view.findViewById(R.id.write) as Button
        write.setOnClickListener {
            val i=Intent(activity,DailyTestIntroActivity::class.java)
            startActivity(i)
        }

        val go_to_links =view.findViewById(R.id.go_to_links) as Button
        go_to_links.setOnClickListener {
            val i=Intent(activity,ImportantLinksActivity::class.java)
            startActivity(i)
        }

        addName()

        return view
    }

    private fun addName(){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid/name")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue(true)!=null) {
                    val word = snapshot.getValue(true).toString()
                }
            }
        })

    }

}