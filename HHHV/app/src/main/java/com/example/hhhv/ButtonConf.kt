package com.example.hhhv

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.ContextCompat

object ButtonConf {
    @SuppressLint("ClickableViewAccessibility")
    fun animationAndDesign(button: Button?, context: Context, text: String) {
        button?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val scaleDown = AnimationUtils.loadAnimation(context, R.anim.anim_scale_down)
                    v.startAnimation(scaleDown)
                }
                MotionEvent.ACTION_UP -> {
                    val scaleUp = AnimationUtils.loadAnimation(context, R.anim.anim_scale_up)
                    v.startAnimation(scaleUp)
                }
            }
            false
        }
        button?.backgroundTintList = null
        button?.background = ContextCompat.getDrawable(context, R.drawable.my_button)
        button?.setTextColor(ContextCompat.getColor(context, R.color.white))
        button?.text = text
    }
}