package com.dogood.tamilneet.chat

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dogood.tamilneet.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_layout_file.view.*
import java.util.*

class ChatActivity : AppCompatActivity() {
    var postList:MutableList<chatData> = mutableListOf()
    val postDataList: ArrayList<String> = ArrayList()
    var unitsList: List<String> = ArrayList()
    private lateinit var mInterstitialAd: InterstitialAd

    lateinit var which_one_dialogue:Dialog
    lateinit var exam_doubt_dialogue:Dialog
    lateinit var choose_subject_dialogue:Dialog
    lateinit var new_question_dialogue:Dialog
    lateinit var progressBar: ProgressDialog


    private lateinit var layoutManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.layoutManager = layoutManager

        which_one_dialogue= Dialog(this)
        exam_doubt_dialogue=Dialog(this)
        choose_subject_dialogue=Dialog(this)
        new_question_dialogue=Dialog(this)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                super.onAdClosed()
                val i=Intent(applicationContext, HomeActivity::class.java)
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

        which_one_dialogue.setContentView(R.layout.which_one_dialogue_layout)
        exam_doubt_dialogue.setContentView(R.layout.exam_doubts_dialogue_layout)
        choose_subject_dialogue.setContentView(R.layout.choose_subject_layout)
        new_question_dialogue.setContentView(R.layout.send_ques_dialogue_layout)

        val write=findViewById(R.id.write) as FloatingActionButton

        write.setOnClickListener {
            newPost()
        }

        addPost()
    }

    private fun newPost(){

        which_one_dialogue.show()

        val exam_doubts=which_one_dialogue.findViewById(R.id.exam_doubts) as Button
        val new_question=which_one_dialogue.findViewById(R.id.new_question) as Button

        exam_doubts.setOnClickListener {
            which_one_dialogue.dismiss()
            val i=Intent(applicationContext,ExamDoubtActivity::class.java)
            startActivity(i)
        }
        new_question.setOnClickListener {
            which_one_dialogue.dismiss()
            val i=Intent(applicationContext,NewQuestionActivity::class.java)
            startActivity(i)
        }
    }





    private fun addPost() {

        val ref = FirebaseDatabase.getInstance().getReference("/Posts/posts")
        var name: String

        postList = mutableListOf()

        val dataListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(chatData::class.java)

                for (shot: DataSnapshot in snapshot.children) {
                    if (data != null) {
                        shot.getValue(chatData::class.java)?.let { postList.add(it) }
                    }
                }
                progressBar.dismiss()

                addData()
            }
        }
        ref.addValueEventListener(dataListener)
    }

    private fun addData() {
        var word:String=""
        for (i in postList) {
            word=i.uid+"\n"
            word=word+i.name+"\n"
            word=word+i.uri+"\n"
            word=word+i.subject+"\n"
            word=word+i.question+"\n"
            word=word+i.option1+"\n"
            word=word+i.option2+"\n"
            word=word+i.option3+"\n"
            word=word+i.option4+"\n"
            word=word+i.answer+"\n"
            word=word+i.like+"\n"
            word=word+i.dislike+"\n"
            word=word+i.comment+"\n"
            word=word+i.postTime+"\n"

            postDataList.add(word)

        }

        recyclerview.setHasFixedSize(true);
        recyclerview.setItemViewCacheSize(20);
        recyclerview.setDrawingCacheEnabled(true);

        recyclerview.adapter = ChatActivity.ChapterAdapter(this, postDataList)
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

    class ChapterAdapter(private val context: ChatActivity, private val postData: ArrayList<String>) : RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {
        val con = context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.chat_layout_file, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return postData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val postDataList=postData.get(position).lines()

            holder.name?.text = postDataList.get(1)




            holder.subject?.text = postDataList.get(3)
            holder.question?.text = postDataList.get(4)
            if(postDataList.get(5).equals("none"))
                holder.option1?.visibility=View.GONE
            else
                holder.option1?.text = postDataList.get(5)
            if(postDataList.get(6).equals("none"))
                holder.option2?.visibility = View.GONE
            else
                holder.option2?.text = postDataList.get(6)
            if(postDataList.get(7).equals("none"))
                holder.option3?.visibility = View.GONE
            else
                holder.option3?.text = postDataList.get(7)
            if(postDataList.get(8).equals("none"))
                holder.option4?.visibility = View.GONE
            else
                holder.option4?.text = postDataList.get(8)
            if(postDataList.get(9).equals("none"))
                holder.answer?.visibility = View.GONE
            else
                holder.answer?.text = postDataList.get(9)
            holder.timeOfPost?.text=postDataList.get(13)

            Picasso.get().load(postDataList.get(2)).into(holder.profile_pic)

        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name=view.name
            val subject=view.subject
            val question=view.question_name
            val option1=view.option1
            val option2=view.option2
            val option3=view.option3
            val option4=view.option4
            val answer=view.answer
            val profile_pic=view.profile_pic
            val timeOfPost=view.timeOfPost
        }
    }


}

