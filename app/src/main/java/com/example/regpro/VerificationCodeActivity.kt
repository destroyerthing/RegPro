package com.example.regpro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationCodeActivity : AppCompatActivity() {
    private lateinit var etVerificationCode: EditText
    private lateinit var btnVerifyCode: Button
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)
        etVerificationCode = findViewById(R.id.etVerificationCode)
        btnVerifyCode = findViewById(R.id.btnVerifyCode)
        verificationId = intent.getStringExtra("verificationId") ?: ""

        btnVerifyCode.setOnClickListener {
            val code = etVerificationCode.text.toString().trim()
            if (code.isNotEmpty()) {
                verifyCode(code)
            } else {
                etVerificationCode.error = "Введите код"
            }
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успешная авторизация
                    Toast.makeText(this@VerificationCodeActivity, "Verification Success", Toast.LENGTH_SHORT).show()
                } else {
                    // Ошибка авторизации
                    Toast.makeText(this@VerificationCodeActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
