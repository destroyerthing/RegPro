package com.example.regpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnNext: Button
    private lateinit var spinnerCountry: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            etPhoneNumber = findViewById(R.id.etPhoneNumber)
            btnNext = findViewById(R.id.btnNext)
            spinnerCountry = findViewById(R.id.spinnerCountry)

            btnNext.setOnClickListener {
                val phoneNumber = etPhoneNumber.text.toString().trim()
                val countryCode = "+7"
                if (phoneNumber.isNotEmpty()) {
                    val fullPhoneNumber = countryCode + phoneNumber
                    sendVerificationCode(fullPhoneNumber)
                } else {
                    etPhoneNumber.error = "Введите номер телефона"
                }
            }
        }

        private fun sendVerificationCode(phoneNumber: String) {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                        val intent = Intent(this@MainActivity, VerificationCodeActivity::class.java)
                        intent.putExtra("verificationId", verificationId)
                        startActivity(intent)
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Успешная авторизация
                        Toast.makeText(this@MainActivity, "Verification Success", Toast.LENGTH_SHORT).show()
                    } else {
                        // Ошибка авторизации
                        Toast.makeText(this@MainActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
