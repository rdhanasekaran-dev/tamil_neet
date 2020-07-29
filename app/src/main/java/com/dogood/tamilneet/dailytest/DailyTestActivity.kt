package com.dogood.tamilneet.dailytest

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.dogood.tamilneet.HomeActivity
import com.dogood.tamilneet.R
import com.dogood.tamilneet.User
import com.dogood.tamilneet.model.QuestionData
import com.dogood.tamilneet.test.TestResultActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_daily_test.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.added_info_dialogue_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DailyTestActivity : AppCompatActivity() {
    var questionsList:MutableList<QuestionData> = mutableListOf()
    lateinit var likedList:ArrayList<String>
    lateinit var dislikedList:ArrayList<String>
    lateinit var currentQuestion:QuestionData
    var i=0
    var j=0
    var correct_answer=-1
    var wrong_answer=-1
    var skipped_answer=0
    lateinit var radio: RadioButton
    lateinit var radio1: RadioButton
    lateinit var radio_button1: RadioButton
    lateinit var radio_button2: RadioButton
    lateinit var radio_button3: RadioButton
    lateinit var radio_button4: RadioButton
    lateinit var radio_button5: RadioButton
    lateinit var radio_button6: RadioButton
    lateinit var issue_dialogue:Dialog
    lateinit var added_info_dialogue:Dialog
    lateinit var completed_dialogue:Dialog
    lateinit var question_no:TextView
    lateinit var issue_content:EditText
    var no=1
    var issue_report=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_test)
        likedList= arrayListOf()
        dislikedList= arrayListOf()

        radio_button1=findViewById(R.id.radio_button1)
        radio_button2=findViewById(R.id.radio_button2)
        radio_button3=findViewById(R.id.radio_button3)
        radio_button4=findViewById(R.id.radio_button4)
        radio_button5=findViewById(R.id.radio_button5)
        radio_button6=findViewById(R.id.radio_button6)

        question_no=findViewById(R.id.question_no)

        issue_dialogue= Dialog(this)
        added_info_dialogue= Dialog(this)
        completed_dialogue= Dialog(this)
        issue_dialogue.setContentView(R.layout.issue_dialogue_layout)
        added_info_dialogue.setContentView(R.layout.added_info_dialogue_layout)
        completed_dialogue.setContentView(R.layout.test_completed_dialogue_layout)

        completed_dialogue.setCanceledOnTouchOutside(false)

        val ok=issue_dialogue.findViewById(R.id.ok) as Button
        val cancel=issue_dialogue.findViewById(R.id.cancel) as Button
        val cancel_info=added_info_dialogue.findViewById(R.id.cancel) as Button
        val rb1=issue_dialogue.findViewById(R.id.radio_button1) as RadioButton
        val rb2=issue_dialogue.findViewById(R.id.radio_button2) as RadioButton
        val rb3=issue_dialogue.findViewById(R.id.radio_button3) as RadioButton
        val rb4=issue_dialogue.findViewById(R.id.radio_button4) as RadioButton
        val rb5=issue_dialogue.findViewById(R.id.radio_button5) as RadioButton
        val rb6=issue_dialogue.findViewById(R.id.radio_button6) as RadioButton
        val rb7=issue_dialogue.findViewById(R.id.radio_button7) as RadioButton
        val rb8=issue_dialogue.findViewById(R.id.radio_button8) as RadioButton


        issue.setOnClickListener {
            issue_dialogue.show()
            val issue_radio_group1=issue_dialogue.findViewById(R.id.issue_radio_group) as RadioGroup
            issue_radio_group1.setOnCheckedChangeListener { group, checkedId ->
                radio1=issue_dialogue.findViewById(checkedId)
            }
            ok.setOnClickListener {
              if(rb1.isChecked || rb2.isChecked || rb3.isChecked || rb4.isChecked || rb5.isChecked || rb6.isChecked || rb7.isChecked || rb8.isChecked) {
                  issue_dialogue.dismiss()
                  issue_report=radio1.text.toString()
                  added_info_dialogue.show()

                  val ok=added_info_dialogue.findViewById(R.id.ok) as Button

                  ok.setOnClickListener {
                      issue_content=added_info_dialogue.findViewById(R.id.et_content)
                      val added_info=issue_content.text.toString().trim()
                      if(!added_info.equals("")){
                          issue_report=issue_report+"\n"+added_info
                          postReport()
                      }else{
                          Toast.makeText(applicationContext,"Add some details",Toast.LENGTH_SHORT).show()
                      }
                  }

                  Toast.makeText(applicationContext,issue_report,Toast.LENGTH_SHORT).show()
                  cancel_info.setOnClickListener {
                      added_info_dialogue.dismiss()
                  }
              }else{
                  Toast.makeText(applicationContext,"Select your issue type",Toast.LENGTH_SHORT).show()
              }
            }
            cancel.setOnClickListener {
                issue_dialogue.dismiss()
            }
        }

        next.setOnClickListener {
            if(d_like_on.visibility== View.VISIBLE){
                likedList.add(currentQuestion.id)
            }else if(d_dislike_on.visibility== View.VISIBLE){
                dislikedList.add(currentQuestion.id)
            }

            next.visibility= View.GONE
            skip.visibility= View.VISIBLE
            radio.isChecked=false
            addData()
            radioButtonDefault()
        }

        d_like_off.setOnClickListener {
            d_like_off.visibility=View.GONE
        }


   /*     d_like_on.setOnClickListener {
            d_like_on.visibility= View.INVISIBLE
            d_dislike_off.visibility= View.VISIBLE
            d_dislike_on.visibility= View.INVISIBLE
            d_like_off.visibility= View.VISIBLE
        }
        d_like_off.setOnClickListener {
            d_like_off.visibility= View.INVISIBLE
            d_dislike_off.visibility= View.VISIBLE
            d_dislike_on.visibility= View.INVISIBLE
            d_like_on.visibility= View.VISIBLE
        }
        d_dislike_off.setOnClickListener {
            d_dislike_off.visibility= View.INVISIBLE
            d_like_off.visibility= View.VISIBLE
            d_like_on.visibility= View.INVISIBLE
            d_dislike_on.visibility= View.VISIBLE
        }
        d_dislike_on.setOnClickListener {
            d_dislike_on.visibility= View.INVISIBLE
            d_like_off.visibility= View.VISIBLE
            d_like_on.visibility= View.INVISIBLE
            d_dislike_off.visibility= View.VISIBLE
        }
*/
        addQuestionData()


    }

    private fun addQuestionData(){

        val ref= FirebaseDatabase.getInstance().getReference("/questions/questions")
        questionsList= mutableListOf()

        val dataListener=object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data=snapshot.getValue(QuestionData::class.java)

                for(shot: DataSnapshot in snapshot.children) {
                    if (data != null) {
                        shot.getValue(QuestionData::class.java)?.let { questionsList.add(it) }
                        i=questionsList.size
                    }
                }

                addData()
            }
        }
        ref.addValueEventListener(dataListener)
    }

    private fun addData(){
        if(j<i) {
            question_no.text="கேள்வி "+no

            currentQuestion = questionsList.get(j)
            question.setText(currentQuestion.question)
            getImage()

            if (!currentQuestion.option1.equals("none")) {
                radio_button1.visibility = View.VISIBLE
                radio_button1.isEnabled = true
                t1.visibility = View.VISIBLE
                radio_button1.setText(currentQuestion.option1)
            } else {
                radio_button1.visibility = View.GONE
                t1.visibility = View.GONE
            }
            if (!currentQuestion.option2.equals("none")) {
                radio_button2.visibility = View.VISIBLE
                t2.visibility = View.VISIBLE
                radio_button2.isEnabled = true
                radio_button2.setText(currentQuestion.option2)
            } else {
                radio_button2.visibility = View.GONE
                t2.visibility = View.GONE
            }
            if (!currentQuestion.option3.equals("none")) {
                radio_button3.visibility = View.VISIBLE
                t3.visibility = View.VISIBLE
                radio_button3.isEnabled = true
                radio_button3.setText(currentQuestion.option3)
            } else {
                radio_button3.visibility = View.GONE
                t3.visibility = View.GONE
            }
            if (!currentQuestion.option4.equals("none")) {
                radio_button4.visibility = View.VISIBLE
                t4.visibility = View.VISIBLE
                radio_button4.isEnabled = true
                radio_button4.setText(currentQuestion.option4)
            } else {
                radio_button4.visibility = View.GONE
                t4.visibility = View.GONE
            }
            if (!currentQuestion.option5.equals("none")) {
                radio_button5.visibility = View.VISIBLE
                t5.visibility = View.VISIBLE
                radio_button5.isEnabled = true
                radio_button5.setText(currentQuestion.option5)
            } else {
                radio_button5.visibility = View.GONE
                t5.visibility = View.GONE
            }
            if (!currentQuestion.option6.equals("none")) {
                radio_button6.visibility = View.VISIBLE
                t6.visibility = View.VISIBLE
                radio_button6.isEnabled = true
                radio_button6.setText(currentQuestion.option6)
            } else {
                radio_button6.visibility = View.GONE
                t6.visibility = View.GONE
            }
            j++
            no++
            issue.visibility = View.GONE
            explanation_card.visibility= View.GONE
            your_answer.visibility= View.GONE
            correct.visibility= View.GONE
            wrong.visibility= View.GONE
            next.visibility= View.GONE
            question_dna_card.visibility= View.GONE
            easy.visibility= View.GONE
            medium.visibility= View.GONE
            hard.visibility= View.GONE
            d_like_on.visibility= View.GONE
            d_like_off.visibility= View.VISIBLE
            d_dislike_on.visibility= View.GONE
            d_dislike_off.visibility= View.VISIBLE
            showSolution()
        }else{
            val show_result=completed_dialogue.findViewById(R.id.show_result) as Button
            completed_dialogue.show()

            show_result.setOnClickListener {
                completed_dialogue.dismiss()
            }

            completed_dialogue.setOnDismissListener {
                val i= Intent(applicationContext, TestResultActivity::class.java)
                i.putExtra("correct",correct_answer)
                i.putExtra("wrong",wrong_answer)
                i.putExtra("skipped",skipped_answer)
                i.putStringArrayListExtra("likedList",likedList)
                i.putStringArrayListExtra("dislikedList",dislikedList)
                startActivity(i)
            }

        }


        skip.setOnClickListener {
            skipped_answer+=1
            skip.visibility= View.GONE
            showSkippedSolution()
        }
    }

    private fun getImage(){
        if(!currentQuestion.uri.equals("none")) {
            quesImage.visibility = View.VISIBLE
            Picasso.get().load(currentQuestion.uri).into(quesImage)
        }else{
            quesImage.visibility = View.GONE
        }
    }

    private fun showSolution(){

        radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            radio=findViewById(checkedId)
            your_answer.visibility= View.VISIBLE
            if(currentQuestion.answerNr.equals(radio.text.toString())) {
                correct_answer+=1
                radio.setBackgroundResource(R.drawable.radio_button_green_back)
                correct.visibility= View.VISIBLE
                radioButtonEnabled()
            } else{
                wrong_answer+=1
                radio.setBackgroundResource(R.drawable.radio_button_red_back)
                wrong.visibility= View.VISIBLE
                radioButtonEnabled()
            }
            showQuestionDna()
            showExplanation()
        })

    }

    private fun showSkippedSolution(){

        if(radio_button1.text.equals(currentQuestion.answerNr)){
            radio=radio_button1
            radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(radio_button2.text.equals(currentQuestion.answerNr)){
            radio=radio_button2
            radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(radio_button3.text.equals(currentQuestion.answerNr)){
            radio=radio_button3
            radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(radio_button4.text.equals(currentQuestion.answerNr)){
            radio=radio_button4
            radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(radio_button5.text.equals(currentQuestion.answerNr)){
            radio=radio_button5
            radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }else if(radio_button6.text.equals(currentQuestion.answerNr)){
            radio=radio_button6
            radio.setBackgroundResource(R.drawable.radio_button_green_back)
        }

        showQuestionDna()
        showExplanation()
    }


    private fun showQuestionDna(){
        issue.visibility= View.VISIBLE
        question_dna_card.visibility= View.VISIBLE
        correctPercent.setText(currentQuestion.correctPercent)
        subject.setText(currentQuestion.subject)
        origin.setText(currentQuestion.origin)


        if(currentQuestion.difficulty.equals("easy")){
            easy.visibility= View.VISIBLE
        }else if(currentQuestion.difficulty.equals("medium")){
            medium.visibility= View.VISIBLE
        }else if(currentQuestion.difficulty.equals("hard")){
            hard.visibility= View.VISIBLE
        }
    }

    private fun showExplanation(){
        explanation_card.visibility= View.VISIBLE
        explanation.setText(currentQuestion.explanation)
        next.visibility= View.VISIBLE
        if(!currentQuestion.explanationImage.equals("none")) {
            explainImage.visibility = View.VISIBLE
            Picasso.get().load(currentQuestion.explanationImage).into(explainImage)
        }else{
            explainImage.visibility = View.GONE
        }
    }

    private fun radioButtonEnabled(){
        radio_button1.isEnabled=false
        radio_button2.isEnabled=false
        radio_button3.isEnabled=false
        radio_button4.isEnabled=false
        radio_button5.isEnabled=false
        radio_button6.isEnabled=false
    }

    private fun radioButtonDefault(){
        radio.setBackgroundResource(R.drawable.radio_button_back)
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
        val report = Report(datetime1,uid,issue_report,currentQuestion.question,currentQuestion.subject,currentQuestion.id)

        ref.setValue(report)
            .addOnSuccessListener {

                Toast.makeText(this,"Reported Successfully", Toast.LENGTH_SHORT).show()
                added_info_dialogue.dismiss()
            }
            .addOnFailureListener {

                Toast.makeText(this,"Registered Failed", Toast.LENGTH_SHORT).show()
            }
    }
}


class Report(val date_time:String,val uid:String,val report:String,val question:String,val subject:String,val id:String)