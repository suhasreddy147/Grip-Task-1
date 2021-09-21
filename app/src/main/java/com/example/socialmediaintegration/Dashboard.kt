package com.example.socialmediaintegration

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso

class Dashboard : AppCompatActivity() {

    private lateinit var img : ImageView
    private lateinit var email : TextView
    private lateinit var name : TextView
    private lateinit var sout : Button
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        img = findViewById(R.id.imageView)
        email = findViewById(R.id.tv_email)
        name = findViewById(R.id.tv_name)
        sout = findViewById(R.id.btn_so)

        sout.setOnClickListener {
            signOut()
        }
        val acct: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName: String = acct.displayName
            val personEmail:String = acct.email
            val personPhoto: Uri = acct.photoUrl
            Picasso.get().load(personPhoto).into(img)
            email.text=personEmail
            name.text=personName
        }
        //email.text=intent.extras?.getString("email")
        //name.text=intent.extras?.getString("name")
        //Picasso.get().load(intent.extras?.getString("picture")).into(img)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, OnCompleteListener<Void?>() {
            finish()
        })
    }
}