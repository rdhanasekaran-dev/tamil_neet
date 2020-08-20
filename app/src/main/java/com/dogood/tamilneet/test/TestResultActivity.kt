package com.dogood.tamilneet.test

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dogood.tamilneet.HomeActivity
import com.dogood.tamilneet.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_test_result.*

class TestResultActivity : AppCompatActivity() {

    var correct_answer:Int=0
    var wrong_answer:Int=0
    var skipped:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)


        MobileAds.initialize(this,"ca-app-pub-5918143946387420/7604458083")
        val tradview=findViewById(R.id.tradView) as AdView
        val adRequest= AdRequest.Builder().build()
        tradview.loadAd(adRequest)

        correct_answer=intent.getIntExtra("correct",0)
        wrong_answer=intent.getIntExtra("wrong",0)
        skipped=intent.getIntExtra("skipped",0)
        val likedList=intent.getStringArrayListExtra("likedList")
        val dislikedList=intent.getStringArrayListExtra("dislikedList")


        next.setOnClickListener {
            val i=Intent(applicationContext,HomeActivity::class.java)
            startActivity(i)
        }

        setPieChart()
        setChart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i=Intent(applicationContext,HomeActivity::class.java)
        startActivity(i)
    }


    private fun setChart(){
        val barchart=findViewById(R.id.barchart)as BarChart

        barchart.description.isEnabled=false
        barchart.legend.textSize=8f

        val correctList:ArrayList<BarEntry> = arrayListOf()
        val wrongList:ArrayList<BarEntry> = arrayListOf()
        val skippedList:ArrayList<BarEntry> = arrayListOf()
        val datasetList:ArrayList<IBarDataSet> = arrayListOf()

        correctList.add(BarEntry(1f,correct_answer.toFloat()))
        wrongList.add(BarEntry(2f,wrong_answer.toFloat()))
        skippedList.add(BarEntry(3f,skipped.toFloat()))


        val dataset=BarDataSet(correctList, "சரியான பதில்கள்")
        dataset.color=Color.GREEN
        val dataset1=BarDataSet(wrongList,"தவறான பதில்கள்")
        dataset1.color=Color.RED
        val dataset2=BarDataSet(skippedList,"தவிர்க்கப்பட்டது")
        dataset2.color=Color.YELLOW

        datasetList.add(dataset)
        datasetList.add(dataset1)
        datasetList.add(dataset2)

        val data=BarData(datasetList)
        barchart.setViewPortOffsets(100f,15f,0f,100f)
        barchart.data=data

    }
    private fun setPieChart(){
        val piechart=findViewById(R.id.piechart) as PieChart

        val pieDataSet = PieDataSet(getData(), "chart")
        pieDataSet.setColors(Color.GREEN, 1)
        piechart.holeRadius = 70f


        val pieData = PieData(pieDataSet)

        pieData.setDrawValues(false)


        piechart.description.text = ""
        piechart.legend.isEnabled = false
        piechart.data = pieData
        piechart.invalidate()

    }

    private fun getData(): List<PieEntry> {

        val total=correct_answer+wrong_answer+skipped

        val percent=((correct_answer*100)/total)

        val percent2=100-percent

        result_percent.setText(""+percent+"%")

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(percent.toFloat(), 1))
        entries.add(PieEntry(percent2.toFloat(), 2))
        return entries
    }
}