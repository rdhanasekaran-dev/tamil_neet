package com.dogood.tamilneet.syllabus

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dogood.tamilneet.*
import com.dogood.tamilneet.R
import com.google.android.gms.ads.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_physics_syllabus.*
import kotlinx.android.synthetic.main.material_layout_file.view.*
import kotlinx.android.synthetic.main.syllabus_layout_file.view.*
import java.util.ArrayList

class PhysicsSyllabus : AppCompatActivity() {


    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var progressBar: ProgressDialog

    var syllabusList:MutableList<SyllabusData> = mutableListOf()
    var unitsList: List<String> = ArrayList()
    val chaptersList11: ArrayList<String> = ArrayList()
    val chaptersList12: ArrayList<String> = ArrayList()

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutManager1: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physics_syllabus)

        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val ps2adview=findViewById(R.id.ps2adView) as AdView
        val ad2request= AdRequest.Builder().build()
        ps2adview.loadAd(ad2request)

        val back=findViewById(R.id.back) as LinearLayout

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                super.onAdClosed()
                val i=Intent(applicationContext,HomeActivity::class.java)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        progressBar= ProgressDialog(this)
        progressBar.setCanceledOnTouchOutside(false)
        progressBar.setCancelable(true)
        progressBar.setMessage("பட்டியல் தயாராகிறது...")
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressBar.show()

        progressBar.setOnCancelListener {
            val i=Intent(applicationContext,HomeActivity::class.java)
            finish()
            startActivity(i)
        }

        back.setOnClickListener {
            showAd()
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerview.layoutManager = layoutManager
        layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerview1.layoutManager = layoutManager1

        addSyllabus()
    }

    private fun addSyllabus(){

        val ref= FirebaseDatabase.getInstance().getReference("/Syllabus/syllabus")
        var name:String

        syllabusList= mutableListOf()

        val dataListener=object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(SyllabusData::class.java)

                for(shot: DataSnapshot in snapshot.children){
                    if (data != null) {
                        shot.getValue(SyllabusData::class.java)?.let { syllabusList.add(it) }
                    }
                }
                progressBar.dismiss()
                addData()
            }
        }
        ref.addValueEventListener(dataListener)
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

    private fun addData(){
        for(i in syllabusList){
            if(i.subject.equals("physics")){
                if(i.std.equals("11")){
                    unitsList
                    val unsep_units:String=i.units
                    unitsList=unsep_units.lines()
                    for(i in unitsList) {
                        chaptersList11.add(i)
                    }
                }else if(i.std.equals("12")){
                    unitsList
                    val unsep_units:String=i.units
                    unitsList=unsep_units.lines()
                    for(i in unitsList) {
                        chaptersList12.add(i)
                    }
                }
            }
        }
        recyclerview.adapter = PhysicsSyllabus.ChapterAdapter(this, chaptersList11)
        recyclerview1.adapter = PhysicsSyllabus.ChapterAdapter(this, chaptersList12)
    }

    class ChapterAdapter(private val context: PhysicsSyllabus, private val chaptersList: ArrayList<String>) :
        RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.syllabus_layout_file, parent, false))
        }
        override fun getItemCount(): Int {
            return chaptersList.size
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.chapterName?.text = chaptersList.get(position)
        }
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val chapterName = view.sy_name
        }
    }
}