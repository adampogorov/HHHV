package com.example.hhhv

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hhhv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var worker: Button? = null
    private var client: Button? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        worker = binding.worker
        client = binding.client
        ButtonConf.animation(worker!!,this,"Исполнитель")
        ButtonConf.animation(client!!,this,"Клиент")
        worker?.setOnClickListener {
            move(this, Worker::class.java)
        }
        client?.setOnClickListener {
            move(this, Client::class.java)
        }

    }
    companion object {
        fun move(context: Context, activity: Class<out Activity>) {
            val intent = Intent(context, activity)
            context.startActivity(intent)
        }


    }


}



