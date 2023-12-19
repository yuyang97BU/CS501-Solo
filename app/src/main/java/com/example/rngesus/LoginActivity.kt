package com.example.rngesus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

// put the login here because professor asked me to specifically during presentation

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var editTextEmailAddress: EditText
    private lateinit var editTextPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="Login"
        auth= FirebaseAuth.getInstance()
    }
    //login function
    fun login(view: View){
        editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email=editTextEmailAddress.text.toString()
        val password=editTextPassword.text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){

                val user = auth.currentUser
                // check for email verification
                // still letting them in for now because this app really shouldn't need login
                // put the login here because professor asked me to specifically during presentation
                if (user != null && !user.isEmailVerified) {
                    Toast.makeText(this, R.string.email_not_verified, Toast.LENGTH_LONG).show()
                }

                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    // going to the registration page by clicking on text
    fun goToRegister(view: View){
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}