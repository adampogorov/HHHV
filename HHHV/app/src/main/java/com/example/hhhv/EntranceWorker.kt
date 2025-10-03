package com.example.hhhv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.transition.Visibility
import com.example.hhhv.databinding.ActivityWorkerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class EntranceWorker : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var back: Button? = null
    private var toRegistration: TextView? = null
    private var entrance: Button? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var forgetPassword: TextView? = null
    private var count = 0
    private lateinit var binding: ActivityWorkerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWorkerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        email = binding.emailAuth
        password = binding.passwordAuth
        entrance = binding.entrance
        toRegistration = binding.toregistration
        back = binding.back
        forgetPassword = binding.forgetPassword
        forgetPassword?.visibility = View.GONE

        ButtonConf.animationAndDesign(entrance!!, this, "Войти")
        ButtonConf.animationAndDesign(back!!, this, "Назад")

        toRegistration?.setOnClickListener {
            MainActivity.move(this, RegistrationWorker::class.java)
        }
        entrance?.setOnClickListener {
            val emailText = email?.text.toString()
            val passwordText = password?.text.toString()

            if (passwordText.isEmpty() || emailText.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null && user.isEmailVerified) {
                                startActivity(Intent(this, WorkersOrder::class.java))
                            } else {
                                Toast.makeText(
                                    this,
                                    "Ваш адрес электронной почты не подтвержден,проверьте ваш почтовый ящик",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidCredentialsException) {
                                password?.error = "Неправильные email или пароль"
                                count++
                                if (count == 3) {
                                    forgetPassword?.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
            }
        }
        forgetPassword?.setOnClickListener {
            val emailText = email?.text.toString()
            auth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener { task->
                    if(task.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setMessage("Письмо для сброса пароля отправлено на ваш адрес электронной " +
                                    "почты")
                            .setPositiveButton(android.R.string.ok) {_,_ ->

                            }
                            .show()
                    } else {
                        Toast.makeText(this,"Ошибка ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        back?.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                })
        }
    }
}
