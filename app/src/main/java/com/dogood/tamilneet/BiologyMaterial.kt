package com.dogood.tamilneet

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.main.activity_biology_material.*
import kotlinx.android.synthetic.main.material_layout_file.view.*
import java.lang.reflect.Array
import java.util.ArrayList

class BiologyMaterial : AppCompatActivity() {
    var syllabusList:MutableList<SyllabusData> = mutableListOf()
    val chaptersList11: ArrayList<String> = ArrayList()
    var unitsList: List<String> = ArrayList()
    val unitNumberList1: ArrayList<String> = ArrayList()
    val unitNumberList2: ArrayList<String> = ArrayList()
    val chaptersList12: ArrayList<String> = ArrayList()
    lateinit var progressBar: ProgressDialog

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutManager1: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biology_material)

        val back=findViewById(R.id.back) as LinearLayout


        MobileAds.initialize(this,"ca-app-pub-5918143946387420/4433878576")
        val bmadview=findViewById(R.id.bmadView) as AdView
        val adrequest= AdRequest.Builder().build()
        bmadview.loadAd(adrequest)


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
            val i=Intent(applicationContext,HomeActivity::class.java)
            finish()
            startActivity(i)
        }

        layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerview.layoutManager = layoutManager
        layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        recyclerview1.layoutManager = layoutManager1

        addSyllabus()
    }

    private fun addSyllabus(){

        val ref=FirebaseDatabase.getInstance().getReference("/Syllabus/syllabus")
        var name:String

        syllabusList= mutableListOf()

        val dataListener=object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(SyllabusData::class.java)

                for(shot:DataSnapshot in snapshot.children){
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

    private fun addData(){
        for(i in syllabusList){
            if(i.subject.equals("biology")){
                if(i.std.equals("bot")){
                    unitsList
                    unitNumberList1
                    val unsep_units:String=i.units
                    unitsList=unsep_units.lines()
                    var k=1
                    for(i in unitsList) {
                        unitNumberList1.add("அலகு "+k)
                        chaptersList11.add(i)
                        k++
                    }
                }else if(i.std.equals("zoo")){
                    unitsList
                    unitNumberList2
                    val unsep_units:String=i.units
                    unitsList=unsep_units.lines()
                    var k=1
                    for(i in unitsList) {
                        unitNumberList2.add("அலகு "+k)
                        chaptersList12.add(i)
                        k++
                    }
                }
            }
        }
        recyclerview.adapter = BiologyMaterial.ChapterAdapter(this, chaptersList11,unitNumberList1)
        recyclerview1.adapter = BiologyMaterial.ChapterAdapter(this, chaptersList12,unitNumberList2)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i=Intent(applicationContext,HomeActivity::class.java)
        finish()
        startActivity(i)
    }

    class ChapterAdapter(private val context: BiologyMaterial, private val chaptersList: ArrayList<String>,private val unitNumberList:ArrayList<String>) :
        RecyclerView.Adapter<ChapterAdapter.ViewHolder>(){
        val con=context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.material_layout_file, parent, false))
        }
        override fun getItemCount(): Int {
            return chaptersList.size
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.chapterName?.text = chaptersList.get(position)
            holder.unit_number?.text=unitNumberList.get(position)
            holder.itemView.setOnClickListener {
                val i=Intent(con,WebViewActivity::class.java)
                i.putExtra("value",chaptersList.get(position))

                con.startActivity(i)
            }
        }
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val chapterName = view.unit_name
            val unit_number=view.unit_number
        }
    }
}