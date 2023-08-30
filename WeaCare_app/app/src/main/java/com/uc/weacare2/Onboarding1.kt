package com.uc.weacare2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class Onboarding1 : AppCompatActivity() {
    private lateinit var btn_next1 : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding1)

        btn_next1 = findViewById(R.id.btn_next1)
        btn_next1.setOnClickListener{
            val myIntent = Intent(this, Onboarding2::class.java)
            startActivity(myIntent)
        }
    }
}