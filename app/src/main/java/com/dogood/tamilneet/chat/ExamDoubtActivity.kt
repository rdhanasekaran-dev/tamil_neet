package com.dogood.tamilneet.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dogood.tamilneet.R
import com.dogood.tamilneet.User
import com.dogood.tamilneet.UserData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ExamDoubtActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_doubt)


        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val edmadview=findViewById(R.id.edmadView) as AdView
        val edadrequest= AdRequest.Builder().build()
        edmadview.loadAd(edadrequest)

        getDoubt()
    }

    var name=""

    private fun getDoubt(){
       val post=findViewById(R.id.post_doubt) as Button

        val doubt_text=findViewById(R.id.doubt_edittext) as EditText

        val doubt=doubt_text.text
        post.setOnClickListener {
            if(!doubt.equals("")){

                val calendar= Calendar.getInstance()
                val simpleDataFormat1= SimpleDateFormat("dd-MM-yy / hh:mm a")
                val simpleDataFormat= SimpleDateFormat("ddMMMMYYYYhhmmssa")
                val datetime1=simpleDataFormat1.format(calendar.time).toString()
                val datetime=simpleDataFormat.format(calendar.time)
                val id=datetime.toString()
                val dislikes="0 dislikes"
                val comment="none"
                val subject="exam-doubt"
                val uid = FirebaseAuth.getInstance().uid ?:""
                val ref = FirebaseDatabase.getInstance().getReference("/Posts/posts/$id")
                val uid_im = FirebaseAuth.getInstance().uid ?:""
                val ref_img = FirebaseDatabase.getInstance().getReference("/Users/$uid_im")
                var uri="none"
                var likes=id

                ref_img.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                            val data=snapshot.getValue(UserData::class.java)
                            name=data!!.name
                            uri=data.uri

                            if(!uri.equals("default")){
                                val postData=chatData(uid,name,uri,likes,dislikes,comment,subject,doubt.toString(),"none","none","none","none","none",datetime1)
                                ref.setValue(postData)
                                    .addOnSuccessListener {

                                        Toast.makeText(applicationContext,"Posted successfully", Toast.LENGTH_SHORT).show()
                                        val i=Intent(applicationContext,ChatActivity::class.java)
                                        startActivity(i)
                                    }
                                    .addOnFailureListener {

                                        Toast.makeText(applicationContext,"Posted Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }else{
                                uri="none"
                                val postData=chatData(uid,name,uri,likes,dislikes,comment,subject,doubt.toString(),"none","none","none","none","none",datetime1)
                                ref.setValue(postData)
                                    .addOnSuccessListener {

                                        Toast.makeText(applicationContext,"Posted successfully", Toast.LENGTH_SHORT).show()
                                        val i=Intent(applicationContext,ChatActivity::class.java)
                                        startActivity(i)
                                    }
                                    .addOnFailureListener {

                                        Toast.makeText(applicationContext,"Posted Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                    }
                })




            }else{
                Toast.makeText(applicationContext,"Write your doubts", Toast.LENGTH_SHORT).show()
            }
        }


    }
}