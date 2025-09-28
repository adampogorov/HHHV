package com.example.hhhv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hhhv.databinding.ActivityClientBinding
import com.google.firebase.auth.FirebaseAuth

class Client : AppCompatActivity() {
    private var back: Button? = null
    private var hint: TextView? = null
    private var entrance: Button? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private lateinit var binding: ActivityClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityClientBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        back = binding.backAuth
        hint = binding.hint
        entrance = binding.auth
        email = binding.emailAuth
        password = binding.passwordAuth

        ButtonConf.animation(entrance!!,this,"Войти")
        ButtonConf.animation(back!!,this,"Назад")
        hint?.setOnClickListener {
            MainActivity.move(this, RegistrationClient::class.java)
        }
        entrance?.setOnClickListener {
            val emailText = email?.text.toString()
            val passwordText = password?.text.toString()
            if (passwordText.isEmpty() || emailText.isEmpty()) {
                Toast.makeText(this,"Не все поля заполнены", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            MainActivity.move(this, Workers::class.java)
                        } else {
                            Toast.makeText(
                                this,
                                "Ошибка,неверные логин или пароль",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        back?.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            )
        }

    }
}