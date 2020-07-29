package com.dogood.tamilneet.practice

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.dogood.tamilneet.HomeActivity
import com.dogood.tamilneet.R
import com.dogood.tamilneet.model.QuestionData
import com.dogood.tamilneet.test.TestResultActivity
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_practice.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PracticeActivity : AppCompatActivity() {
    var t_question_list:MutableList<QuestionData> = mutableListOf()
    lateinit var t_current_question: QuestionData

    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var progressBar: ProgressDialog

    lateinit var t_question: TextView
    lateinit var t_ques_image: ImageView
    lateinit var t_questionNo: TextView
    lateinit var t_next: Button
    lateinit var t_skip: Button
    lateinit var t_radio1: RadioButton

    var i=0;var j=0;var no=1

    lateinit var t_issue_dialogue: Dialog
    lateinit var t_added_info_dialogue: Dialog
    lateinit var t_completed_dialogue: Dialog

    var subject_selected=""
    var sub=""

    lateinit var t_issue_content: EditText
    var t_issue_report=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)



        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        val prsadview=findViewById(R.id.prsadView) as AdView
        val adRequest= AdRequest.Builder().build()
        prsadview.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                val i= Intent(applicationContext, HomeActivity::class.java)
                startActivity(i)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        progressBar= ProgressDialog(this)
        progressBar.setCanceledOnTouchOutside(false)
        progressBar.setCancelable(true)
        progressBar.setMessage("கேள்விகள் தயாராகிறது...")
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressBar.show()

        progressBar.setOnCancelListener {
            val i=Intent(applicationContext, HomeActivity::class.java)
            finish()
            startActivity(i)
        }

        t_questionNo=findViewById(R.id.p_question_no)
        t_question=findViewById(R.id.p_question)
        t_ques_image=findViewById(R.id.p_ques_img)
        t_next=findViewById(R.id.p_next)
        t_skip=findViewById(R.id.p_prev)

        t_issue_dialogue= Dialog(this)
        t_issue_dialogue.setContentView(R.layout.issue_dialogue_layout)
        t_added_info_dialogue= Dialog(this)
        t_added_info_dialogue.setContentView(R.layout.added_info_dialogue_layout)
        t_completed_dialogue= Dialog(this)
        t_completed_dialogue.setContentView(R.layout.test_completed_dialogue_layout)

        t_completed_dialogue.setCanceledOnTouchOutside(false)

        val t_ok=t_issue_dialogue.findViewById(R.id.ok) as Button
        val t_cancel=t_issue_dialogue.findViewById(R.id.cancel) as Button
        val t_cancel_info=t_added_info_dialogue.findViewById(R.id.cancel) as Button
        val t_rb1=t_issue_dialogue.findViewById(R.id.radio_button1) as RadioButton
        val t_rb2=t_issue_dialogue.findViewById(R.id.radio_button2) as RadioButton
        val t_rb3=t_issue_dialogue.findViewById(R.id.radio_button3) as RadioButton
        val t_rb4=t_issue_dialogue.findViewById(R.id.radio_button4) as RadioButton
        val t_rb5=t_issue_dialogue.findViewById(R.id.radio_button5) as RadioButton
        val t_rb6=t_issue_dialogue.findViewById(R.id.radio_button6) as RadioButton
        val t_rb7=t_issue_dialogue.findViewById(R.id.radio_button7) as RadioButton
        val t_rb8=t_issue_dialogue.findViewById(R.id.radio_button8) as RadioButton

        subject_selected=intent.getStringExtra("subject")

        if(subject_selected.equals("physics")){
            sub= "இயற்பியல்"
        }else if(subject_selected.equals("chemistry")){
            sub= "வேதியியல்"
        }else if(subject_selected.equals("biology")){
            sub= "உயிரியல்"
        }

        t_next.setOnClickListener {
            addData()
        }

        t_skip.setOnClickListener {
            addPrevData()
        }

        issue.setOnClickListener {
            t_issue_dialogue.show()
            val issue_radio_group1=t_issue_dialogue.findViewById(R.id.issue_radio_group) as RadioGroup
            issue_radio_group1.setOnCheckedChangeListener { group, checkedId ->
                t_radio1=t_issue_dialogue.findViewById(checkedId)
            }
            t_ok.setOnClickListener {
                if(t_rb1.isChecked || t_rb2.isChecked || t_rb3.isChecked || t_rb4.isChecked || t_rb5.isChecked || t_rb6.isChecked || t_rb7.isChecked || t_rb8.isChecked) {
                    t_issue_dialogue.dismiss()
                    t_issue_report=t_radio1.text.toString()
                    t_added_info_dialogue.show()

                    val ok=t_added_info_dialogue.findViewById(R.id.ok) as Button

                    ok.setOnClickListener {
                        t_issue_content=t_added_info_dialogue.findViewById(R.id.et_content)
                        val added_info=t_issue_content.text.toString().trim()
                        if(!added_info.equals("")){
                            t_issue_report=t_issue_report+"\n"+added_info
                            postReport()
                        }else{
                            Toast.makeText(applicationContext,"Add some details",Toast.LENGTH_SHORT).show()
                        }
                    }

                    Toast.makeText(applicationContext,t_issue_report,Toast.LENGTH_SHORT).show()
                    t_cancel_info.setOnClickListener {
                        t_added_info_dialogue.dismiss()
                    }
                }else{
                    Toast.makeText(applicationContext,"Select your issue type",Toast.LENGTH_SHORT).show()
                }
            }
            t_cancel.setOnClickListener {
                t_issue_dialogue.dismiss()
            }
        }

        addQuestionData()

    }

    private fun addQuestionData(){

        val ref= FirebaseDatabase.getInstance().getReference("/questions/questions")

        t_question_list= mutableListOf()

        val dataListener=object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(QuestionData::class.java)

                for(shot: DataSnapshot in snapshot.children) {
                    if (data != null) {

                        shot.getValue(QuestionData::class.java)?.let {
                            if(it.subject.equals(sub)) {
                                t_question_list.add(it)
                            }
                        }
                        t_question_list.shuffle()
                        i=t_question_list.size
                    }
                }
                progressBar.dismiss()
                addData()
            }
        }
        ref.addValueEventListener(dataListener)
    }

    private fun addData(){
        if(j<i) {
            t_questionNo.text="கேள்வி "+no

            t_current_question = t_question_list.get(j)
            t_question.setText(t_current_question.question)
            p_answer.setText(t_current_question.answerNr)
            getImage()

            j++
            no++

        }else{
            val show_result=t_completed_dialogue.findViewById(R.id.show_result) as Button
            t_completed_dialogue.show()

            show_result.setOnClickListener {
                t_completed_dialogue.dismiss()
            }

            t_completed_dialogue.setOnDismissListener {
                showAd()
            }

        }

        showExplanation()
    }
    private fun addPrevData(){
        if(j<i) {
            j-=2
            no-=2
            t_questionNo.text="கேள்வி "+no

            t_current_question = t_question_list.get(j)
            t_question.setText(t_current_question.question)
            p_answer.setText(t_current_question.answerNr)

            j++
            no++

            getImage()

        }else{

        }

        showExplanation()
    }

    private fun getImage(){
        if(!t_current_question.uri.equals("none")) {
            t_ques_image.visibility = View.VISIBLE

            Toast.makeText(applicationContext,"image is loading",Toast.LENGTH_SHORT).show()
            Picasso.get().load(t_current_question.uri).into(p_ques_img)
        }else{
            t_ques_image.visibility = View.GONE
        }
    }

    private fun showExplanation(){
        p_explain.setText(t_current_question.explanation)
        t_next.visibility= View.VISIBLE
        if(!t_current_question.explanationImage.equals("none")) {
            p_explain_img.visibility = View.VISIBLE
            Picasso.get().load(t_current_question.explanationImage).into(p_explain_img)
        }else{
            p_explain_img.visibility = View.GONE
        }
    }
    private fun showAd(){
        if(mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }else{
            val i= Intent(applicationContext, HomeActivity::class.java)
            startActivity(i)
        }
    }

    private fun postReport(){
        val calendar= Calendar.getInstance()
        val simpleDataFormat1= SimpleDateFormat("dd-MM-yy / hh:mm a")
        val simpleDataFormat= SimpleDateFormat("ddMMMMYYYYhhmmssa")
        val datetime1=simpleDataFormat1.format(calendar.time).toString()
        val datetime=simpleDataFormat.format(calendar.time)
        val id=datetime.toString()
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Report/$uid/$id")
        val report = PReported(datetime1,uid,t_issue_report,t_current_question.question,t_current_question.subject,t_current_question.id)

        ref.setValue(report)
            .addOnSuccessListener {

                Toast.makeText(this,"Reported Successfully", Toast.LENGTH_SHORT).show()
                t_added_info_dialogue.dismiss()
            }
            .addOnFailureListener {

                Toast.makeText(this,"Registered Failed", Toast.LENGTH_SHORT).show()
            }
    }

}


class PReported(val date_time:String,val uid:String,val report:String,val question:String,val subject:String,val id:String)