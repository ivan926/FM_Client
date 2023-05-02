package com.bignerdranch.android.client

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
//        //wiring my widgets
//
//        ServerHostField = findViewById(R.id.serverHostField)
//        ServerPortField = findViewById(R.id.serverPortField)
//        usernameField = findViewById(R.id.userNameField)
//        passwordField = findViewById(R.id.passwordField)
//        firstNameField = findViewById(R.id.firstNameField)
//        lastNameField = findViewById(R.id.lastNameField)
//        emailAddressField = findViewById(R.id.EmailAddressField)
//        maleButton = findViewById(R.id.maleButton)
//        femaleButton = findViewById(R.id.femaleButton)
//        radioGroup = findViewById(R.id.radioGroup)
//        signInButton = findViewById(R.id.SignInButton)
//        registerButton = findViewById(R.id.registerButton)
//
//        radioGroup.setOnCheckedChangeListener(
//            RadioGroup.OnCheckedChangeListener{
//
//                group,checkedId->
//                val radio_language: RadioButton = findViewById(checkedId)
//                Toast.makeText(this,radio_language.text,Toast.LENGTH_LONG)
//            }
//        )
//
//        fun radio_button_click() {
//            // Get the clicked radio button instance
//            genderButton = findViewById(radioGroup.checkedRadioButtonId)
//            // Toast.makeText(applicationContext,"On click : ${radio.text}",
//            //  Toast.LENGTH_SHORT).show()
//
//        }
//
//        fun updateButtonState() {
//            val editText1Value = ServerHostField.text.toString()
//            val editText2Value = ServerPortField.text.toString()
//            val editText3Value = usernameField.text.toString()
//            val editText4Value = passwordField.text.toString()
//            val editText5Value = firstNameField.text.toString()
//            val editText6Value = lastNameField.text.toString()
//            val editText7Value = emailAddressField.text.toString()
//
//
//            signInButton.isEnabled = editText1Value.isNotEmpty() && editText2Value.isNotEmpty()
//                    && editText3Value.isNotEmpty() && editText4Value.isNotEmpty()
//
//            registerButton.isEnabled = editText1Value.isNotEmpty() && editText2Value.isNotEmpty()
//                    && editText3Value.isNotEmpty() && editText4Value.isNotEmpty()
//                    && editText4Value.isNotEmpty() && editText5Value.isNotEmpty()
//                    && editText6Value.isNotEmpty() && editText7Value.isNotEmpty()
//                    &&  (genderButton != null)
//        }
//
//        maleButton.setOnClickListener{
//            // Get the checked radio button id from radio group
//            var id: Int = radioGroup.checkedRadioButtonId
//            if (id!=-1){ // If any radio button checked from radio group
//                // Get the instance of radio button using id
//                val radio:RadioButton = findViewById(id)
//             //   Toast.makeText(applicationContext,"On button click : ${radio.text}",
//               //     Toast.LENGTH_SHORT).show()
//
//                radio_button_click()
//                updateButtonState()
//
//            }else{
//                // If no radio button checked in this radio group
//            //    Toast.makeText(applicationContext,"On button click : nothing selected",
//              //      Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        femaleButton.setOnClickListener{
//            // Get the checked radio button id from radio group
//            var id: Int = radioGroup.checkedRadioButtonId
//            if (id!=-1){ // If any radio button checked from radio group
//                // Get the instance of radio button using id
//                val radio:RadioButton = findViewById(id)
//              //  Toast.makeText(applicationContext,"On button click : ${radio.text}",
//                //    Toast.LENGTH_SHORT).show()
//
//                radio_button_click()
//                updateButtonState()
//            }else{
//                // If no radio button checked in this radio group
//               // Toast.makeText(applicationContext,"On button click : nothing selected",
//                 //   Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//
//
//        signInButton.isEnabled = false
//        registerButton.isEnabled = false
//
//
//
//        ServerHostField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//
//        ServerPortField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//        usernameField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//        passwordField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//
//        firstNameField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//        lastNameField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//
//        emailAddressField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                updateButtonState()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Do nothing
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Do nothing
//            }
//        })
//
//        signInButton.setOnClickListener{
//
//            val hostName = ServerHostField.text.toString()
//            val portNum = ServerPortField.text.toString()
//            val username = usernameField.text.toString()
//            val password = passwordField.text.toString()
//
//
//
//                val mHandler : Handler
//               // val handler = Handler(Looper.getMainLooper())
//                val message : Message
//
//
//
//                val handler = object:  Handler(Looper.getMainLooper()) {
//                    override fun handleMessage(msg: Message) {
//                        val bundle : Bundle = msg.data
//                        val firstname : String
//                        val lastname : String
//                        if(bundle.containsKey("error"))
//                        {
//                            val error = bundle.getString("error")
//
//                            Toast.makeText(this@MainActivity,error,Toast.LENGTH_LONG).show()
//
//                        }
//                        else {
//                            firstname = bundle.getString("firstname", "Not Found")
//                            lastname = bundle.getString("lastname", "Last name not found")
//                            val fullname = firstname + " " + lastname;
//                            Toast.makeText(this@MainActivity, fullname, Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//            val login = loginBackGroundTask(handler,hostName,portNum,username,password)
//
//            val execute : ExecutorService = Executors.newSingleThreadExecutor()
//
//            execute.submit(login)
//
//
//
//        }
//
//        registerButton.setOnClickListener{
//            val hostName = ServerHostField.text.toString()
//            val portNum = ServerPortField.text.toString()
//            val username = usernameField.text.toString()
//            val password = passwordField.text.toString()
//            val firstName = firstNameField.text.toString()
//            val lastName = lastNameField.text.toString()
//            val emailAddr = emailAddressField.text.toString()
//            var gender = genderButton!!.text.toString()
//
//            gender = gender[0].toString()
//
//            val handler = object:  Handler(Looper.getMainLooper()) {
//                override fun handleMessage(msg: Message) {
//                    val bundle : Bundle = msg.data
//                    val firstName : String
//                    val lastName : String
//
//                    if(bundle.containsKey("error"))
//                    {
//                        val error = bundle.getString("error")
//
//                        Toast.makeText(this@MainActivity,error,Toast.LENGTH_LONG).show()
//
//                    }
//                    else {
//                        firstName = bundle.getString("firstname", "No firstName")
//                        lastName = bundle.getString("lastname", "No last name")
//                        var toast: Toast
//                        // toast = Toast.makeText(this@MainActivity,firstName,Toast.LENGTH_LONG)
//                        //  toast.setGravity(2,10,0)
//                        //  toast.show()
//                        val fullName: String
//                        fullName = firstName + " " + lastName
//                        Toast.makeText(this@MainActivity, fullName, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//
//            val register = RegisterBackgroundTask(handler,hostName,portNum,username,password,emailAddr,firstName,lastName,gender)
//
//            val execute : ExecutorService = Executors.newSingleThreadExecutor()
//
//            execute.submit(register)
//
//
//
//
//
//
//
//
//            val toastMessage = Toast.makeText(this,"You have registered congratulations",Toast.LENGTH_LONG)
//
////           // toastMessage.show()
//
//
//        }
//
//




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
        supportFragmentManager.beginTransaction().add(R.id.activity_main,Mapsfragment)
            .commit()
    }

    public fun switchBackToLoginActivity()
    {
        val loginFrag = loginFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_main,loginFrag)
            .commit()
    }

}