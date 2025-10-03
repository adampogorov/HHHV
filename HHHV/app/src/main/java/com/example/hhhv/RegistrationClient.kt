package com.example.hhhv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hhhv.databinding.ActivityRegistrationClientBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationClient : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var back: Button? = null
    private var emailReg: EditText? = null
    private var passwordReg: EditText? = null
    private var registrationButton: Button? = null
    private lateinit var binding: ActivityRegistrationClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistrationClientBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        emailReg = binding.emailReg
        passwordReg = binding.passwordReg
        registrationButton = binding.registration
        back = binding.backReg

        ButtonConf.animationAndDesign(registrationButton, this, "Зарегистрироваться")
        ButtonConf.animationAndDesign(back, this, "Назад")

        back?.setOnClickListener {
            startActivity(
                Intent(this, EntranceClient::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            )
        }
        registrationButton?.setOnClickListener {
            val emailText = emailReg?.text.toString()
            val passwordText = passwordReg?.text.toString()
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            } else {
                register(emailText,passwordText)
            }
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
            val user = auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { verification ->
                        if(verification.isSuccessful) {
                            Toast.makeText(this,"Письмо для подтверждения отправлено на ваш email $email",
                                Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, EntranceClient::class.java))
                            finish()
                        }
                    }

            } else {
                Toast.makeText(
                    this, "Ошибка: ${task.exception?.message}", Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}
