package com.dogood.tamilneet

import android.graphics.Color
import android.os.Bundle
import android.provider.Contacts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntegerRes
import com.dogood.tamilneet.chat.chatData
import com.dogood.tamilneet.test.Performance
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_quiz.*
import java.lang.NumberFormatException

class QuizFragment : Fragment() {

    var PerformanceList:MutableList<Performance> = mutableListOf()
    var r=0
    var w=0
    var t=0
    lateinit var performanceText:TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        MobileAds.initialize(context,"ca-app-pub-3940256099942544/6300978111")
        val fqadview=view.findViewById(R.id.fqadView) as AdView
        val fqadRequest= AdRequest.Builder().build()
        fqadview.loadAd(fqadRequest)

        performanceText=view.findViewById(R.id.performancetext)

        val uid = FirebaseAuth.getInstance().uid ?:""

        val ref = FirebaseDatabase.getInstance().getReference("/Performance/performance/$uid")
        PerformanceList= mutableListOf()
        val dataListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Performance::class.java)

                for (shot: DataSnapshot in snapshot.children) {
                    if (data != null) {
                        shot.getValue(Performance::class.java)?.let { PerformanceList.add(it) }
                    }
                }

                try {
                    for (i in PerformanceList) {
                        r = r + i.right.toInt()
                        w = w + i.wrong.toInt()
                    }
                }catch (e: NumberFormatException){
                    r=0
                    w=0
                }
                t=r+w
                val pichart = view.findViewById(R.id.piechart) as PieChart
                val pieDataSet = PieDataSet(putData(), "chart")
                pichart.visibility=View.VISIBLE
                pieDataSet.setColors(Color.GREEN, 1)
                pichart.holeRadius = 70f
                val lineData = PieData(pieDataSet)
                lineData.setDrawValues(false)

                pichart.description.text = ""
                pichart.legend.isEnabled = false
                pichart.data = lineData
                pichart.invalidate()

            }
        }
        ref.addValueEventListener(dataListener)



        return view
    }
    private fun putData():List<PieEntry>{
        if(t==0) {
            t=1
            val rp = (r * 100) / t
            val wp = (w * 100) / t
            performanceText.setText(""+rp+" %")
            val entries = ArrayList<PieEntry>()
            entries.add(PieEntry(rp.toFloat(), 1))
            entries.add(PieEntry(wp.toFloat(), 2))
            return entries
        }else{
            val rp = (r * 100) / t
            val wp = (w * 100) / t
            performanceText.setText(""+rp+" %")
            val entries = ArrayList<PieEntry>()
            entries.add(PieEntry(rp.toFloat(), 1))
            entries.add(PieEntry(wp.toFloat(), 2))
            return entries
        }
    }
}