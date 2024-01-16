package com.best.free.todo.task.manager.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.best.free.todo.task.manager.R

class SplashScreenActivity : AppCompatActivity() {
    private var splashTime: Long = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashScreenActivity,MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        },splashTime)
    }
}