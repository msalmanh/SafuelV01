package com.example.muhammadsalman.safuelv01

import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mAuth: FirebaseAuth

    var verificationId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        OtpSubmit.setOnClickListener {
            authenticate()
        }
        loginButton.setOnClickListener {
            verify()
        }
    }
    /* fun VerifyUserButton(view: View){

        verify()
    }
    fun OnsubmitOtp(view: View){
        authenticate()
    }*/

    private fun verify(){
        verificationCallbacks()

        /*val countryCode = ccp.selectedCountryNameCode.toString()
        val ccpAndPhone = countryCode+phnNo
        Log.d("concet","The number n0w is"+ccpAndPhone)
        Log.d("CCP","country code is "+countryCode )
        Log.d("Tag","Phone Number is " + phnNo)*/



            val phnNo = phonetext.text.toString()
            val countryCode = ccp.selectedCountryCode.toString()
            val ccpAndPhone = "+"+countryCode+phnNo
            Log.d("conteCode","the country code is "+countryCode)
            Log.d("contTwo","the total phone number is now "+ccpAndPhone)
        if(ccpAndPhone.length>=13) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ccpAndPhone,
                60,
                TimeUnit.SECONDS,
                this@MainActivity,
                mCallbacks)
        }
        else{
            toast("Enter Valid Number")

        }



    }

    private fun verificationCallbacks(){
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){


            override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
                Log.d("Tag","onVerificationCompleted")
                signIn(credential)
            }

            override fun onVerificationFailed(e: FirebaseException?) {
                    Log.d("TagOne","onVerificationFailed is "+ e.toString())
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            override fun onCodeSent(verification: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verification, p1)
                Log.d("Tag", "onCodeSent is called $verification")
                headerLayout.visibility = View.GONE
                headerLayoutOne.visibility = View.VISIBLE
                PhoneNumberLayout.visibility = View.GONE
                PhoneNumberLayoutOne.visibility = View.VISIBLE
                verifyPhoneNumberLayout.visibility = View.GONE
                verifyPhoneNumberLayoutOne.visibility = View.VISIBLE
                verificationId = verification.toString()
                var resendToken = p1
                //authenticate()


            }



        }


    }

    private fun signIn(credential: PhoneAuthCredential?){
        headerLayout.visibility = View.GONE
        headerLayoutOne.visibility = View.VISIBLE
        PhoneNumberLayout.visibility = View.GONE
        PhoneNumberLayoutOne.visibility = View.VISIBLE
        verifyPhoneNumberLayout.visibility = View.GONE
        verifyPhoneNumberLayoutOne.visibility = View.VISIBLE
        mAuth.setLanguageCode("En")
        if (credential != null) {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) {
                        task ->
                    if (task.isSuccessful){
                        toast("Login successful !!")
                        val user = task.result.user
                        startActivity(Intent(this, MainSecondActivity::class.java))
                    }
                    else{
                        toast("Login Failed !!")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                }
        }

    }

    private fun toast (msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun authenticate () {
        Log.d("TagSentCode","The function is being called ")

        val verifiNo = OtpFieldOne.text.toString()
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verifiNo)

        signIn(credential)

        //startActivity(Intent(this, MainSecondActivity::class.java))

    }
}
