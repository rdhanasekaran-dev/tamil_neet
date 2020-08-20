package com.dogood.tamilneet

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dogood.tamilneet.chat.ChatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.main.activity_physics_material.*
import kotlinx.android.synthetic.main.choose_subject_layout.*
import kotlinx.android.synthetic.main.fragment_social.*
import kotlinx.android.synthetic.main.material_layout_file.view.*
import kotlinx.android.synthetic.main.which_one_dialogue_layout.*
import java.util.ArrayList

class SocialFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.fragment_social, container, false)

        val ok=view.findViewById(R.id.ok) as Button
        ok.setOnClickListener {
            val i=Intent(context,ChatActivity::class.java)
            startActivity(i)
        }

        MobileAds.initialize(context,"ca-app-pub-5918143946387420/1599121232")
        val fsadview=view.findViewById(R.id.fsadView) as AdView
        val fsadRequest= AdRequest.Builder().build()
        fsadview.loadAd(fsadRequest)

        return view
    }


}