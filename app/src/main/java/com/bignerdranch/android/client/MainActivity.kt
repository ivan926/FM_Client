package com.bignerdranch.android.client

import android.os.*
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private  var genderButton: RadioButton? = null
    private lateinit var ServerHostField: EditText
    private lateinit var ServerPortField: EditText
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var emailAddressField: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var maleButton: RadioButton
    private lateinit var femaleButton: RadioButton
    private lateinit var signInButton: Button
    private lateinit var registerButton: Button

    private var logInCounter : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //inflating the layout
         setContentView(R.layout.activity_main)



        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main)

        if(currentFragment == null)
        {
            val fragment = loginFragment()
            supportFragmentManager.beginTransaction().add(R.id.activity_main,fragment)
                .commit()
        }







    }


     fun switchToMapFragment()
    {
        val Mapsfragment = google_map_fragment()
        supportFragmentManager.beginTransaction().replace(R.id.activity_main,Mapsfragment)
            .commit()
    }



    public fun switchBackToLoginActivity()
    {
        val loginFrag = loginFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_main,loginFrag)
            .commit()
    }

}