package com.example.socialmediaintegration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fb_btn : LoginButton
    lateinit var callbackManager: CallbackManager
    private val EMAIL = listOf("email","public_profile")
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var g_btn : SignInButton
    private var RC_SIGN_IN = 100
    var flag=0
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        fb_btn = findViewById(R.id.login_button)
        g_btn = findViewById(R.id.sign_in_button)
        name = findViewById(R.id.fb_name)
        email = findViewById(R.id.fb_email)
        img = findViewById(R.id.imageView3)

        g_btn.setOnClickListener{
            signIn()
            flag=1
        }

        fb_btn.setOnClickListener{
            flag = 2
            fb_btn.setReadPermissions(EMAIL)
            callbackManager = CallbackManager.Factory.create()
            g_btn.visibility = View.GONE
            g_btn.isEnabled = false

            LoginManager.getInstance().registerCallback(callbackManager, object:FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    val bundle = Bundle()
                    val graphRequest = GraphRequest.newMeRequest(result?.accessToken){obj, response ->
                        try{
                            if(obj.has("id")){
                                email.text=obj.getString("email")
                                name.text=obj.getString("name")
                                val personPhoto: String = "https://graph.facebook.com/${obj.getString("id")}/picture?width=200&height=200"
                                Picasso.get().load(personPhoto).into(img)
                                /*bundle.putString("email",obj.getString("email"))
                                bundle.putString("name",obj.getString("name"))*/
                                Log.d("FACEBOOKDATA", obj.getString("name"))
                                Log.d("FACEBOOKDATA", obj.getString("email"))
                                Log.d("FACEBOOKDATA", obj.getString("picture"))
                            }
                        }
                        catch (e: Exception){}
                    }
                    val param = Bundle()
                    param.putString("fields","name,email,id,picture.type(large)")
                    graphRequest.parameters = param
                    graphRequest.executeAsync()
                    /*val intent= Intent(this@MainActivity, Dashboard::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)*/
                }

                override fun onCancel() {
                    Toast.makeText(applicationContext,"Login Cancelled",Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(applicationContext,"Login Error",Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun signIn() {
        val intent : Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(flag==2) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
        if(flag==1){
            if(requestCode==RC_SIGN_IN){
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val acct: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName: String = acct.displayName
                val personGivenName: String = acct.givenName
                val personFamilyName:String = acct.familyName
                val personEmail:String = acct.email
                val personId:String = acct.id
                val personPhoto: Uri = acct.photoUrl
            }
            val intent= Intent(this@MainActivity, Dashboard::class.java)
            startActivity(intent)
        } catch (e: ApiException) {}
    }
}