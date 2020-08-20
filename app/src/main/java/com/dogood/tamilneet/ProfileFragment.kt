package com.dogood.tamilneet

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment() {
    lateinit var profile_image_pic:CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        MobileAds.initialize(context,"ca-app-pub-5918143946387420/3460542407")
        val pfadview=view.findViewById(R.id.pfadView) as AdView
        val pfadRequest= AdRequest.Builder().build()
        pfadview.loadAd(pfadRequest)

        val signout=view.findViewById(R.id.signout) as Button
        profile_image_pic=view.findViewById(R.id.profile_image_pick)

        signout.setOnClickListener {
            Toast.makeText(activity, "Signed out", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            val i=Intent(activity,LoginActivity::class.java)
            activity?.finish()
            startActivity(i)
        }

        val ppolicy=view.findViewById(R.id.ppolicy) as FloatingActionButton

        ppolicy.setOnClickListener {
            val i=Intent(activity,PrivacyPolicyActivity::class.java)
            startActivity(i)
        }

        showProfile()

        profile_image_pic.setOnClickListener {
            val i=Intent(Intent.ACTION_PICK)
            i.type="image/*"
            startActivityForResult(i,0)
        }
        return view
    }
    var selected_photo_uri:Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && data!=null && resultCode==Activity.RESULT_OK){
            selected_photo_uri= data.data
            var cr:ContentResolver =activity!!.contentResolver
            val bitmap=MediaStore.Images.Media.getBitmap(cr,selected_photo_uri)
            profile_image_pic.setImageBitmap(bitmap)
            Toast.makeText(context,"one",Toast.LENGTH_SHORT).show()
            uploadImageToFireBaseStorage()
        }
    }

    private fun uploadImageToFireBaseStorage(){
        Toast.makeText(context,"two",Toast.LENGTH_SHORT).show()
        if (selected_photo_uri==null){
            Toast.makeText(activity,"null",Toast.LENGTH_SHORT).show()
            return
        }
         val filename=UUID.randomUUID().toString()
        Toast.makeText(context,filename,Toast.LENGTH_SHORT).show()
         val ref=FirebaseStorage.getInstance().getReference("/profileImages/$filename")
         ref.putFile(selected_photo_uri!!)
             .addOnSuccessListener {
                 Toast.makeText(context,"Uploaded",Toast.LENGTH_SHORT).show()
                 ref.downloadUrl.addOnSuccessListener {

                     val uid = FirebaseAuth.getInstance().uid ?:""
                     val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid/uri")
                     ref.setValue(it.toString())
                     Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show()
                 }


             }
    }

    private fun showProfile(){

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref_img = FirebaseDatabase.getInstance().getReference("/Users/$uid/uri")
        val ref_name = FirebaseDatabase.getInstance().getReference("/Users/$uid/name")
        val ref_email = FirebaseDatabase.getInstance().getReference("/Users/$uid/email")

        ref_img.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.getValue(true).toString().equals("default")){
                    Picasso.get().load(snapshot.getValue(true).toString()).into(profile_image_pic)
                }
            }
        })
        ref_name.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    display_name_show.setText(snapshot.getValue(true).toString())
            }
        })
        ref_email.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    email_id_show.setText(snapshot.getValue(true).toString())
            }
        })
    }
}
