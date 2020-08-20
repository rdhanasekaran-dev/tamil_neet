package com.dogood.tamilneet.testFile

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
import com.dogood.tamilneet.test.Performance
import com.dogood.tamilneet.test.TestResultActivity
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_common_test.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommonTestActivity : AppCompatActivity() {
    var t_question_list:MutableList<QuestionData> = mutableListOf()
    lateinit var t_liked_list:ArrayList<String>
    lateinit var t_disliked_list:ArrayList<String>
    lateinit var t_current_question: QuestionData

    private lateinit var mInterstitialAd: InterstitialAd


    lateinit var t_question: TextView
    lateinit var t_ques_image: ImageView
    lateinit var t_radioButton1: RadioButton
    lateinit var t_radioButton2: RadioButton
    lateinit var t_radioButton3: RadioButton
    lateinit var t_radioButton4: RadioButton
    lateinit var t_radioButton5: RadioButton
    lateinit var t_radioButton6: RadioButton
    lateinit var t_radioGroup: RadioGroup
    lateinit var t_questionNo: TextView
    lateinit var t_next: Button
    lateinit var t_skip: Button
    lateinit var t_radio: RadioButton
    lateinit var t_radio1: RadioButton

    lateinit var progressBar: ProgressDialog


    var i=0;var j=0;var no=1

    var t_correct_answer=0
    var t_wrong_answer=0
    var t_skipped_answer=0


    var liked_id=""
    var disliked_id=""

    lateinit var t_issue_dialogue: Dialog
    lateinit var t_added_info_dialogue: Dialog
    lateinit var t_completed_dialogue: Dialog

    var subject_selected=""
    var num=""
    var num_of_questions=0
    var sub=""

    lateinit var t_issue_content: EditText
    var t_issue_report=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_test)


        MobileAds.initialize(this,"ca-app-pub-5918143946387420/2038104936")
        val ctadview=findViewById(R.id.ctadView) as AdView
        val adRequest= AdRequest.Builder().build()
        ctadview.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-5918143946387420/1654961556"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener=object : AdListener(){
            override fun onAdClosed() {
                val i= Intent(applicationContext, TestResultActivity::class.java)
                i.putExtra("correct",t_correct_answer)
                i.putExtra("wrong",t_wrong_answer)
                i.putExtra("skipped",t_skipped_answer)
                i.putStringArrayListExtra("likedList",t_liked_list)
                i.putStringArrayListExtra("dislikedList",t_disliked_list)
                finish()
                startActivity(i)

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


        t_liked_list= arrayListOf()
        t_disliked_list= arrayListOf()

        t_questionNo=findViewById(R.id.c_question_no)
        t_question=findViewById(R.id.c_question)
        t_ques_image=findViewById(R.id.c_quesImage)
        t_radioButton1=findViewById(R.id.c_radio_button1)
        t_radioButton2=findViewById(R.id.c_radio_button2)
        t_radioButton3=findViewById(R.id.c_radio_button3)
        t_radioButton4=findViewById(R.id.c_radio_button4)
        t_radioButton5=findViewById(R.id.c_radio_button5)
        t_radioButton6=findViewById(R.id.c_radio_button6)
        t_radioGroup=findViewById(R.id.c_radio_group)
        t_next=findViewById(R.id.c_next)
        t_skip=findViewById(R.id.c_skip)

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
        num=intent.getStringExtra("num")

        if(num.equals("15 கேள்விகள்")){
            num_of_questions=15
        }else if(num.equals("30 கேள்விகள்")){
            num_of_questions=30
        }else if(num.equals("50 கேள்விகள்")){
            num_of_questions=50
        }

        if(subject_selected.equals("physics")){
            sub= "இயற்பியல்"
        }else if(subject_selected.equals("chemistry")){
            sub= "வேதியியல்"
        }else if(subject_selected.equals("biology")){
            sub= "உயிரியல்"
        }

        questiont.setText(sub+" தேர்வு")

        t_next.setOnClickListener {
            if(c_like_on.visibility== View.VISIBLE){
                t_liked_list.add(t_current_question.id)
                liked_id+="\n"+t_current_question.id

            }else if(c_dislike_on.visibility== View.VISIBLE){
                t_disliked_list.add(t_current_question.id)
                disliked_id+="\n"+t_current_question.id

            }

            t_next.visibility= View.GONE
            t_skip.visibility= View.VISIBLE
            t_radio.isChecked=false
            addData()
            radioButtonDefault()
        }

        c_issue.setOnClickListener {
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
                    }
                }
                progressBar.dismiss()

                addData()
            }
        }
        ref.addValueEventListener(dataListener)
    }

    private fun addData(){
        if(j<num_of_questions) {
            t_questionNo.text="கேள்வி "+no

            t_current_question = t_question_list.get(j)
            t_question.setText(t_current_question.question)
            getImage()

            if (!t_current_question.option1.equals("none")) {
                t_radioButton1.visibility = View.VISIBLE
                t_radioButton1.isEnabled = true
                c_t1.visibility = View.VISIBLE
                t_radioButton1.setText(t_current_question.option1)
            } else {
                t_radioButton1.visibility = View.GONE
                c_t1.visibility = View.GONE
            }
            if (!t_current_question.option2.equals("none")) {
                t_radioButton2.visibility = View.VISIBLE
                c_t2.visibility = View.VISIBLE
                t_radioButton2.isEnabled = true
                t_radioButton2.setText(t_current_question.option2)
            } else {
                t_radioButton2.visibility = View.GONE
                c_t2.visibility = View.GONE
            }
            if (!t_current_question.option3.equals("none")) {
                t_radioButton3.visibility = View.VISIBLE
                c_t3.visibility = View.VISIBLE
                t_radioButton3.isEnabled = true
                t_radioButton3.setText(t_current_question.option3)
            } else {
                t_radioButton3 .visibility = View.GONE
                c_t3.visibility= View.GONE
            }
            if (!t_current_question.option4.equals("none")) {
                t_radioButton4.visibility = View.VISIBLE
                c_t4.visibility = View.VISIBLE
                t_radioButton4.isEnabled = true
                t_radioButton4.setText(t_current_question.option4)
            } else {
                t_radioButton4.visibility = View.GONE
                c_t4.visibility = View.GONE
            }
            if (!t_current_question.option5.equals("none")) {
                t_radioButton5.visibility = View.VISIBLE
                c_t5.visibility = View.VISIBLE
                t_radioButton5.isEnabled = true
                t_radioButton5.setText(t_current_question.option5)
            } else {
                t_radioButton5.visibility = View.GONE
                c_t5.visibility = View.GONE
            }
            if (!t_current_question.option6.equals("none")) {
                t_radioButton6.visibility = View.VISIBLE
                c_t6.visibility = View.VISIBLE
                t_radioButton6.isEnabled = true
                t_radioButton6.setText(t_current_question.option6)
            } else {
                t_radioButton6.visibility = View.GONE
                c_t6.visibility = View.GONE
            }
            j++
            no++
            c_issue.visibility = View.GONE
            c_explanation_card.visibility= View.GONE
            c_your_answer.visibility= View.GONE
            c_correct.visibility= View.GONE
            c_wrong.visibility= View.GONE
            t_next.visibility= View.GONE
            c_question_dna_card.visibility= View.GONE
            c_easy.visibility= View.GONE
            c_medium.visibility= View.GONE
            c_hard.visibility= View.GONE
            c_like_on.visibility= View.GONE
            c_like_off.visibility= View.VISIBLE
            c_dislike_on.visibility= View.GONE
            c_dislike_off.visibility= View.VISIBLE
            showSolution()
            showSolution()
        }else{
            val show_result=t_completed_dialogue.findViewById(R.id.show_result) as Button
            t_completed_dialogue.show()

            show_result.setOnClickListener {
                t_completed_dialogue.dismiss()
            }

            t_completed_dialogue.setOnDismissListener {
                val calendar= Calendar.getInstance()
                val simpleDataFormat= SimpleDateFormat("ddMMMMYYYYhhmmssa")
                val datetime=simpleDataFormat.format(calendar.time)
                val id=datetime.toString()


                val uid = FirebaseAuth.getInstance().uid ?:""
                val ref = FirebaseDatabase.getInstance().getReference("/Performance/performance/$uid/$id")

                val likedSize=t_liked_list.size.toString()
                val dislikedSize=t_disliked_list.size.toString()


                val performanceData= Performance(uid,dislikedSize,likedSize,disliked_id,liked_id,""+t_correct_answer,""+t_wrong_answer,t_skipped_answer.toString(),t_current_question.subject.toString())

                ref.setValue(performanceData).addOnSuccessListener {
                    showAd()
                }
            }

        }


        t_skip.setOnClickListener {
            t_skipped_answer+=1
            t_skip.visibility= View.GONE
            showSkippedSolution()
        }
    }

    private fun getImage(){
        if(!t_current_question.uri.equals("none")) {
            t_ques_image.visibility = View.VISIBLE
            Toast.makeText(applicationContext,"image is loading",Toast.LENGTH_SHORT).show()
            Picasso.get().load(t_current_question.uri).into(c_quesImage)
        }else{
            t_ques_image.visibility = View.GONE
        }
    }

    private fun showSolution(){

        t_radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            t_radio=findViewById(checkedId)
            c_your_answer.visibility= View.VISIBLE
            if(t_current_question.answerNr.equals(t_radio.text.toString())) {
                t_correct_answer+=1
                t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
                c_correct.visibility= View.VISIBLE
                radioButtonEnabled()
            } else{
                t_wrong_answer+=1
                t_radio.setBackgroundResource(R.drawable.radio_button_red_back)
                c_wrong.visibility= View.VISIBLE
                radioButtonEnabled()
            }
            showQuestionDna()
            showExplanation()
        })

    }

    private fun showSkippedSolution(){

        if(t_radioButton1.text.equals(t_current_question.answerNr)){
            t_radio=t_radioButton1
            t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(t_radioButton2.text.equals(t_current_question.answerNr)){
            t_radio=t_radioButton2
            t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(t_radioButton3.text.equals(t_current_question.answerNr)){
            t_radio=t_radioButton3
            t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(t_radioButton4.text.equals(t_current_question.answerNr)){
            t_radio=t_radioButton4
            t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(t_radioButton5.text.equals(t_current_question.answerNr)){
            t_radio=t_radioButton5
            t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(t_radioButton6.text.equals(t_current_question.answerNr)){
            t_radio=t_radioButton6
            t_radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }

        showQuestionDna()
        showExplanation()
    }



    private fun showQuestionDna(){
        c_issue.visibility= View.VISIBLE
        c_question_dna_card.visibility= View.VISIBLE
        c_correctPercent.setText(t_current_question.correctPercent)
        c_subject.setText(t_current_question.subject)
        c_origin.setText(t_current_question.origin)


        if(t_current_question.difficulty.equals("easy")){
            c_easy.visibility= View.VISIBLE
        }else if(t_current_question.difficulty.equals("medium")){
            c_medium.visibility= View.VISIBLE
        }else if(t_current_question.difficulty.equals("hard")){
            c_hard.visibility= View.VISIBLE
        }
    }

    private fun showExplanation(){
        c_explanation_card.visibility= View.VISIBLE
        c_explanation.setText(t_current_question.explanation)
        t_next.visibility= View.VISIBLE
        if(!t_current_question.explanationImage.equals("none")) {
            c_explainImage.visibility = View.VISIBLE
            Picasso.get().load(t_current_question.explanationImage).into(c_explainImage)
        }else{
            c_explainImage.visibility = View.GONE
        }
    }
    private fun radioButtonEnabled(){
        t_radioButton1.isEnabled=false
        t_radioButton2.isEnabled=false
        t_radioButton3.isEnabled=false
        t_radioButton4.isEnabled=false
        t_radioButton5.isEnabled=false
        t_radioButton6.isEnabled=false
    }

    private fun radioButtonDefault(){
        t_radio.setBackgroundResource(R.drawable.radio_button_back)
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
        val report = Reported(datetime1,uid,t_issue_report,t_current_question.question,t_current_question.subject,t_current_question.id)

        ref.setValue(report)
            .addOnSuccessListener {

                Toast.makeText(this,"Reported Successfully", Toast.LENGTH_SHORT).show()
                t_added_info_dialogue.dismiss()
            }
            .addOnFailureListener {

                Toast.makeText(this,"Registered Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAd(){
        if(mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }else{
            val i= Intent(applicationContext, TestResultActivity::class.java)
            i.putExtra("correct",t_correct_answer)
            i.putExtra("wrong",t_wrong_answer)
            i.putExtra("skipped",t_skipped_answer)
            i.putStringArrayListExtra("likedList",t_liked_list)
            i.putStringArrayListExtra("dislikedList",t_disliked_list)
            finish()
            startActivity(i)
        }
    }

}


class Reported(val date_time:String,val uid:String,val report:String,val question:String,val subject:String,val id:String)