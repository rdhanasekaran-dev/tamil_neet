package com.dogood.tamilneet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var email=""
    var password=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signup.setOnClickListener {
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {

            email = email_edit_text.text.toString()
            password = password_edit_text.text.toString()
           if(email.equals("")){
                Toast.makeText(applicationContext,"Enter your email address",Toast.LENGTH_SHORT).show()
            }else if(password.equals("")){
                Toast.makeText(applicationContext,"Enter your password",Toast.LENGTH_SHORT).show()
            }else{
               login()
           }
        }
    }

    private fun login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) {
                    Toast.makeText(this, "Login is unsuccessful", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                val intent= Intent(this,HomeActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to login : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}