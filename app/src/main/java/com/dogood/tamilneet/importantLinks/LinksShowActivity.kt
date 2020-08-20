package com.dogood.tamilneet.importantLinks

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dogood.tamilneet.R
import com.dogood.tamilneet.SyllabusData
import com.dogood.tamilneet.WebViewActivity
import com.dogood.tamilneet.model.LinkData
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_links_show.*
import kotlinx.android.synthetic.main.links_layout_file.view.*
import java.util.ArrayList
import kotlin.coroutines.coroutineContext

class LinksShowActivity : AppCompatActivity() {
    var link_type=""
    var links_list: ArrayList<String> = arrayListOf()
    private lateinit var layoutManager_link: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_links_show)


        MobileAds.initialize(this,"ca-app-pub-5918143946387420/7483547891")
        val lsadview=findViewById(R.id.lsadView) as AdView
        val adrequest= AdRequest.Builder().build()
        lsadview.loadAd(adrequest)

        link_type=intent.getStringExtra("link_type")


        layoutManager_link = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        link_recycler_view.layoutManager = layoutManager_link

        addSyllabus()

    }


    private fun addSyllabus() {

        val ref = FirebaseDatabase.getInstance().getReference("/Links/links")
        var name: String

        links_list= arrayListOf()

        val dataListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var data = snapshot.getValue(LinkData::class.java)
                for (shot: DataSnapshot in snapshot.children) {
                    if (data != null) {
                        shot.getValue(LinkData::class.java)?.let {
                            if(link_type.equals(it.type)) {
                                links_list.add(it.name)
                            }
                        }
                    }
                }
                addData()
            }
        }
        ref.addValueEventListener(dataListener)
    }

    private fun addData() {

        link_recycler_view.adapter = LinksShowActivity.LinkAdapter(this, links_list)
    }


    class LinkAdapter(private val context: LinksShowActivity, private val LinkList: ArrayList<String>) : RecyclerView.Adapter<LinkAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.links_layout_file, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return LinkList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.LinkName?.text = LinkList.get(position)
            holder.itemView.setOnClickListener {
                val i= Intent(context,WebViewActivity::class.java)
                i.putExtra("value",LinkList.get(position))
                context.startActivity(i)
            }
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val LinkName = view.link_button
        }
    }
}
