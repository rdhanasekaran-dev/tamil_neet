package com.dogood.tamilneet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_material.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        isLoggedIn()
        displayName()

        val adapter=MyViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(MaterialFragment())
        adapter.addFragment(QuizFragment())
        adapter.addFragment(SocialFragment())
        adapter.addFragment(ProfileFragment())
        viewPager.adapter=adapter

        viewPager!!.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //alerta("menu",position.toString())
            }

            override fun onPageSelected(position: Int) {
                bottomNavigationView!!.getMenu().getItem(position).setChecked(true)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })



        bottomNavigationView.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.bottomNavigationGroupMenuId -> {
                    viewPager.setCurrentItem(0)
                    true
                }
                R.id.bottomNavigationStatusMenuId -> {
                    viewPager.setCurrentItem(1)
                    true
                }
                R.id.bottomNavigationSocialMenuId-> {
                    viewPager.setCurrentItem(2)
                    true
                }
                R.id.bottomNavigationProfileMenuId -> {
                    viewPager.setCurrentItem(3)
                    true
                }
                else -> false
            }
        }
    }

    private fun isLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun displayName() {
        val uid = FirebaseAuth.getInstance().uid
        var userDataList: MutableList<UserData>
        var name_:String

        if (uid != null) {
            userDataList = mutableListOf()
            val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")
            val dataListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(UserData::class.java)
                    data?.let { userDataList.add(it) }
                    name_= userDataList.get(0).name


                }
            }
            ref.addValueEventListener(dataListener)
        }
    }

    class MyViewPagerAdapter(manager: FragmentManager) :FragmentPagerAdapter(manager){

        private val fragmentList:MutableList<Fragment> =ArrayList()

        override fun getItem(position: Int): Fragment{
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment){
            fragmentList.add(fragment)
        }

    }


}