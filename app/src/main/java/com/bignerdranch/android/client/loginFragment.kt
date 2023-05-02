package com.bignerdranch.android.client

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class loginFragment : Fragment() {

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

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val view = inflater.inflate(R.layout.activity_fragment, container, false)


        return view

    }

    override fun onStart() {
        super.onStart()

        val fragment_context = getActivity()
        val view : View? = getView()

        //Use method below to get the main view for this fragment
        //requireView()

        ServerHostField = view!!.findViewById(R.id.serverHostField)
        ServerPortField = view.findViewById(R.id.serverPortField)
        usernameField = view.findViewById(R.id.userNameField)
        passwordField = view.findViewById(R.id.passwordField)
        firstNameField = view.findViewById(R.id.firstNameField)
        lastNameField = view.findViewById(R.id.lastNameField)
        emailAddressField = view.findViewById(R.id.EmailAddressField)
        maleButton = view.findViewById(R.id.maleButton)
        femaleButton = view.findViewById(R.id.femaleButton)
        radioGroup = view.findViewById(R.id.radioGroup)
        signInButton = view.findViewById(R.id.SignInButton)
        registerButton = view.findViewById(R.id.registerButton)

        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{

                    group,checkedId->
                val radio_language: RadioButton = view.findViewById(checkedId)
                Toast.makeText(fragment_context,radio_language.text, Toast.LENGTH_LONG)
            }
        )

        fun radio_button_click() {
            // Get the clicked radio button instance
            genderButton = view.findViewById(radioGroup.checkedRadioButtonId)
            // Toast.makeText(applicationContext,"On click : ${radio.text}",
            //  Toast.LENGTH_SHORT).show()

        }

        fun updateButtonState() {
            val editText1Value = ServerHostField.text.toString()
            val editText2Value = ServerPortField.text.toString()
            val editText3Value = usernameField.text.toString()
            val editText4Value = passwordField.text.toString()
            val editText5Value = firstNameField.text.toString()
            val editText6Value = lastNameField.text.toString()
            val editText7Value = emailAddressField.text.toString()


            signInButton.isEnabled = editText1Value.isNotEmpty() && editText2Value.isNotEmpty()
                    && editText3Value.isNotEmpty() && editText4Value.isNotEmpty()

            registerButton.isEnabled = editText1Value.isNotEmpty() && editText2Value.isNotEmpty()
                    && editText3Value.isNotEmpty() && editText4Value.isNotEmpty()
                    && editText4Value.isNotEmpty() && editText5Value.isNotEmpty()
                    && editText6Value.isNotEmpty() && editText7Value.isNotEmpty()
                    &&  (genderButton != null)
        }

        maleButton.setOnClickListener{
            // Get the checked radio button id from radio group
            var id: Int = radioGroup.checkedRadioButtonId
            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio:RadioButton = view.findViewById(id)
                //   Toast.makeText(applicationContext,"On button click : ${radio.text}",
                //     Toast.LENGTH_SHORT).show()

                radio_button_click()
                updateButtonState()

            }else{
                // If no radio button checked in this radio group
                //    Toast.makeText(applicationContext,"On button click : nothing selected",
                //      Toast.LENGTH_SHORT).show()
            }
        }

        femaleButton.setOnClickListener{
            // Get the checked radio button id from radio group
            var id: Int = radioGroup.checkedRadioButtonId
            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio:RadioButton = view.findViewById(id)
                // Toast.makeText(applicationContext,"On button click : ${radio.text}",
                //    Toast.LENGTH_SHORT).show()

                radio_button_click()
                updateButtonState()
            }else{
                // If no radio button checked in this radio group
                // Toast.makeText(applicationContext,"On button click : nothing selected",
                //   Toast.LENGTH_SHORT).show()
            }
        }




        signInButton.isEnabled = false
        registerButton.isEnabled = false



        ServerHostField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })


        ServerPortField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        usernameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })


        firstNameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        lastNameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })


        emailAddressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        signInButton.setOnClickListener{

            val hostName = ServerHostField.text.toString()
            val portNum = ServerPortField.text.toString()
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()



            val mHandler : Handler
            // val handler = Handler(Looper.getMainLooper())
            val message : Message



            val handler = object:  Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    val bundle : Bundle = msg.data
                    val firstname : String
                    val lastname : String
                    if(bundle.containsKey("error"))
                    {

                        val error = bundle.getString("error")

                        Toast.makeText(fragment_context,error, Toast.LENGTH_LONG).show()

                    }
                    else {
                        firstname = bundle.getString("firstname", "Not Found")
                        lastname = bundle.getString("lastname", "Last name not found")
                        val fullname = firstname + " " + lastname;


                       // Toast.makeText(fragment_context, fullname, Toast.LENGTH_LONG).show()


                        (activity as MainActivity).switchToMapFragment()
                    }
                }
            }

            val login = loginBackGroundTask(handler,hostName,portNum,username,password)

            val execute : ExecutorService = Executors.newSingleThreadExecutor()

            execute.submit(login)










        }

        registerButton.setOnClickListener{
            val hostName = ServerHostField.text.toString()
            val portNum = ServerPortField.text.toString()
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val firstName = firstNameField.text.toString()
            val lastName = lastNameField.text.toString()
            val emailAddr = emailAddressField.text.toString()
            var gender = genderButton!!.text.toString()

            gender = gender[0].toString()

            val handler = object:  Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    val bundle : Bundle = msg.data
                    val firstName : String
                    val lastName : String

                    if(bundle.containsKey("error"))
                    {
                        val error = bundle.getString("error")

                        Toast.makeText(fragment_context,error, Toast.LENGTH_LONG).show()

                    }
                    else {
                        firstName = bundle.getString("firstname", "No firstName")
                        lastName = bundle.getString("lastname", "No last name")
                        var toast: Toast
                        // toast = Toast.makeText(this@MainActivity,firstName,Toast.LENGTH_LONG)
                        //  toast.setGravity(2,10,0)
                        //  toast.show()
                        val fullName: String
                        fullName = firstName + " " + lastName
                        Toast.makeText(fragment_context, fullName, Toast.LENGTH_LONG).show()
                    }
                }
            }

            val register = RegisterBackgroundTask(handler,hostName,portNum,username,password,emailAddr,firstName,lastName,gender)

            val execute : ExecutorService = Executors.newSingleThreadExecutor()

            execute.submit(register)








            val toastMessage = Toast.makeText(fragment_context,"You have registered congratulations", Toast.LENGTH_LONG)

//           // toastMessage.show()


        }

        Log.d("OnStart","starting onStart lifeCycle method")


    }








}