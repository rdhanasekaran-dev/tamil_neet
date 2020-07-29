package com.dogood.tamilneet.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dogood.tamilneet.R
import com.dogood.tamilneet.UserData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.send_ques_dialogue_layout.*
import java.text.SimpleDateFormat
import java.util.*

class NewQuestionActivity : AppCompatActivity() {
    var subject=""
    var question=""
    var option1=""
    var option2=""
    var option3=""
    var option4=""
    var answer=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_question)


        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val nqmadview=findViewById(R.id.nqmadView) as AdView
        val nqadrequest= AdRequest.Builder().build()
        nqmadview.loadAd(nqadrequest)

        addNewQuestion()
    }
    var name = ""
    private fun addNewQuestion(){
        val post=findViewById(R.id.send_new_question) as Button

        post.setOnClickListener {
            val edit_subject = findViewById(R.id.edit_text_subject) as TextInputEditText
            val edit_question = findViewById(R.id.edit_text_question) as TextInputEditText
            val edit_option1 = findViewById(R.id.edit_text_option1) as TextInputEditText
            val edit_option2 = findViewById(R.id.edit_text_option2) as TextInputEditText
            val edit_option3 = findViewById(R.id.edit_text_option3) as TextInputEditText
            val edit_option4 = findViewById(R.id.edit_text_option4) as TextInputEditText
            val edit_answer = findViewById(R.id.edit_text_answer) as TextInputEditText

            subject = edit_subject.text.toString().trim()
            question = edit_question.text.toString().trim()
            option1 = "A. "+edit_option1.text.toString().trim()
            option2 = "B. "+edit_option2.text.toString().trim()
            option3 = "C. "+edit_option3.text.toString().trim()
            option4 = "D. "+edit_option4.text.toString().trim()
            answer = edit_answer.text.toString().trim()

            if (subject.equals("")) {
                Toast.makeText(applicationContext, "Write subject", Toast.LENGTH_SHORT).show()
            }
            if (question.equals("")) {
                Toast.makeText(applicationContext, "Write question", Toast.LENGTH_SHORT).show()
            }
            if (option1.equals("")) {
                option1 = "none"
            }
            if (option2.equals("")) {
                option2 = "none"
            }
            if (option3.equals("")) {
                option3 = "none"
            }
            if (option4.equals("")) {
                option4 = "none"
            }
            if (answer.equals("")) {
                Toast.makeText(applicationContext, "Write answer", Toast.LENGTH_SHORT).show()
            }


            if (!(subject.equals(""))) {
                if (!(question.equals(""))) {
                    if (!(answer.equals(""))) {
                        val calendar = Calendar.getInstance()
                        val simpleDataFormat1 = SimpleDateFormat("dd-MM-yy / hh:mm a")
                        val simpleDataFormat = SimpleDateFormat("ddMMMMYYYYhhmmssa")
                        val datetime1 = simpleDataFormat1.format(calendar.time).toString()
                        val datetime = simpleDataFormat.format(calendar.time)
                        val id = datetime.toString()
                        var uri = "none"
                        name = ""
                        val likes = id
                        val dislikes = "0 dislikes"
                        val comment = "none"
                        val uid = FirebaseAuth.getInstance().uid ?: ""
                        val ref = FirebaseDatabase.getInstance().getReference("/Posts/posts/$id")


                        val uid_im = FirebaseAuth.getInstance().uid ?:""
                        val ref_img = FirebaseDatabase.getInstance().getReference("/Users/$uid_im")

                        ref_img.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val data=snapshot.getValue(UserData::class.java)
                                name=data!!.name
                                uri=data.uri.toString()


                                if(!uri.equals("default")){
                                    val postData = chatData(uid, name.toString(), uri, likes, dislikes, comment, subject, question, option1, option2, option3, option4, answer, datetime1)
                                    ref.setValue(postData)
                                        .addOnSuccessListener {

                                            Toast.makeText(applicationContext,"Posted successfully", Toast.LENGTH_SHORT).show()
                                            val i=Intent(applicationContext,ChatActivity::class.java)
                                            startActivity(i)
                                        }
                                        .addOnFailureListener {

                                            Toast.makeText(applicationContext,"Posted Failed", Toast.LENGTH_SHORT).show()
                                        }
                                }else {
                                    uri = "none"
                                    val postData = chatData(uid, name, uri, likes, dislikes, comment, subject, question, option1, option2, option3, option4, answer, datetime1)
                                    ref.setValue(postData)
                                        .addOnSuccessListener {

                                            Toast.makeText(applicationContext, "Posted successfully", Toast.LENGTH_SHORT).show()
                                            val i = Intent(applicationContext, ChatActivity::class.java)
                                            startActivity(i)
                                        }
                                        .addOnFailureListener {

                                            Toast.makeText(applicationContext, "Posted Failed", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        })
                    }else{
                        Toast.makeText(applicationContext, "write answer", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "write question", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext, "write subject", Toast.LENGTH_SHORT).show()
            }
        }
    }
}