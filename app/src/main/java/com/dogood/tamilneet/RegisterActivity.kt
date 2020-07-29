package com.dogood.tamilneet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dogood.tamilneet.test.Performance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var email=""
    var password=""
    var name=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        login.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        log_back.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {

            email = edit_text_email.text.toString()
            password = edit_text_password.text.toString()
            name = edit_text_display_name.text.toString()

            if(email.equals("")){
                Toast.makeText(applicationContext,"Enter your email address",Toast.LENGTH_SHORT).show()
            }else if(password.equals("")){
                Toast.makeText(applicationContext,"Enter your password address",Toast.LENGTH_SHORT).show()
            }else if(name.equals("")){
                Toast.makeText(applicationContext,"Enter your name address",Toast.LENGTH_SHORT).show()
            }else{
                perform_register()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun perform_register(){

        if(email.isEmpty()||password.isEmpty()||name.isEmpty()){
            Toast.makeText(this,"Please fill all details", Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) {
                    Toast.makeText(this, "Registration is unsuccessful", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                saveUserToDatabase()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to create user : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToDatabase(){

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")
        val calendar= Calendar.getInstance()
        val simpleDataFormat= SimpleDateFormat("ddMMMMYYYYhhmmssa")
        val datetime=simpleDataFormat.format(calendar.time)
        val id=datetime.toString()

        val ref1 = FirebaseDatabase.getInstance().getReference("/Performance/performance/$uid/$id")

        val perofrmance=Performance(uid,"0","0","0","0","0","0","0","0")

        val user = User(uid,edit_text_display_name.text.toString(),edit_text_email.text.toString(),"default")

            val display_name = edit_text_display_name.text.toString()

        ref.setValue(user)
            .addOnSuccessListener {
                ref1.setValue(perofrmance)
                Toast.makeText(this,"Registered successfully", Toast.LENGTH_SHORT).show()

                val intent= Intent(this,HomeActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {

                Toast.makeText(this,"Registered Failed", Toast.LENGTH_SHORT).show()
            }

    }
}

class User(val uid:String,val name:String,val email:String,val uri:String)