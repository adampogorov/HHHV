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
        emailReg = binding.emailReg
        passwordReg = binding.passwordReg
        registrationButton = binding.registration
        back = binding.backReg

        ButtonConf.animation(registrationButton,this,"Зарегистрироваться")
        ButtonConf.animation(back,this,"Назад")

        back?.setOnClickListener {
            startActivity(
                Intent(this, Client::class.java).apply {
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
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailText, passwordText
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Аккаунт создан!", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(
                            this, "Ошибка: ${task.exception?.message}", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }
}