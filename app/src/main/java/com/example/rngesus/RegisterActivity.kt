package com.example.rngesus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
// put the registration here because professor asked me to specifically during presentation
// reference: https://github.com/emineinan/FirebaseAuthKotlin/tree/acb917b0ff937ea9f6a76a6eb60bc2f88caf746f/app/src/main/java/com/example/firebaseauthapp

class RegisterActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var editTextEmailAddress: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title="Register"
        auth= FirebaseAuth.getInstance()
    }
    // register function
    fun register(view: View){
        editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email=editTextEmailAddress.text.toString()
        val password=editTextPassword.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                user?.sendEmailVerification() // send verification email
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
    // going to the login page by clicking on text
    fun goToLogin(view: View){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

}