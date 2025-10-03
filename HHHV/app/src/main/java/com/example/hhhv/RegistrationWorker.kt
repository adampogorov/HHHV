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
import com.example.hhhv.databinding.ActivityRegistrtionWorkerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegistrationWorker : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var back: Button? = null
    private var emailReg: EditText? = null
    private var passwordReg: EditText? = null
    private var registrationButton: Button? = null
    private lateinit var binding: ActivityRegistrtionWorkerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistrtionWorkerBinding.inflate(layoutInflater)
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

        ButtonConf.animationAndDesign(registrationButton,this,"Регистрация")
        ButtonConf.animationAndDesign(back,this,"Назад")

        back?.setOnClickListener {
            startActivity(
                Intent(this, EntranceWorker::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                })
        }
        registrationButton?.setOnClickListener {
            val emailText = emailReg?.text.toString().trim()
            val passwordText = passwordReg?.text.toString().trim()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(emailText,passwordText)
            }
        }
    }
    private fun registerUser(email: String,password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verification ->
                            if(verification.isSuccessful) {
                                Toast.makeText(this,"Письмо для подтверждения отправлено на ваш email $email",
                                    Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, EntranceWorker::class.java))
                                    finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Ошибка отправки письма: ${verification.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                } else {
                    handleRegistrationError(task.exception)
                }
            }

    }
    private fun handleRegistrationError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthWeakPasswordException -> {
                Toast.makeText(this, "Слабый пароль. Минимум 6 символов", Toast.LENGTH_LONG).show()
            }
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(this, "Пользователь с таким email уже существует", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Ошибка: ${exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}