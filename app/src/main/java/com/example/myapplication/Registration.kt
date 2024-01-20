package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registration : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = Firebase.auth

        val loginText: TextView = findViewById(R.id.textView2)

        loginText.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val registerbutton: Button = findViewById(R.id.button)

        registerbutton.setOnClickListener {
            signUPS()
        }
    }

    private fun signUPS(){
        val username = findViewById<EditText>(R.id.usernameReg)
        val password = findViewById<EditText>(R.id.PasswordReg)

        val inputNam = username.text.toString()
        val inputPass = password.text.toString()

        if(username.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Please fill in",Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(inputNam, inputPass).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(baseContext, "Registration Failed", Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener {
            Toast.makeText(this,"Error ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
        }

    }
}