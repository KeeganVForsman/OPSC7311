package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val registrationText: TextView = findViewById(R.id.textView4)

        registrationText.setOnClickListener {
            val intent = Intent(this,Registration::class.java)
            startActivity(intent)
        }

        val loginBUT: Button = findViewById(R.id.button2)

        loginBUT.setOnClickListener {
            login_perform()
        }
    }

    private fun login_perform(){

        val user: EditText = findViewById(R.id.username_Login)
        val password: EditText = findViewById(R.id.password_Login)

        if(user.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"fill in",Toast.LENGTH_SHORT).show()
            return
        }

        val usernameINput = user.text.toString()
        val passwordINput = password.text.toString()

        auth.signInWithEmailAndPassword(usernameINput, passwordINput).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(
                    baseContext,"success",
                    Toast.LENGTH_SHORT
                ).show()

            }else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener {
            Toast.makeText(baseContext,"Error. ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
        }
    }
}