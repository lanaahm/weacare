package com.uc.weacare2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Onboarding2 : AppCompatActivity() {
    private lateinit var start_btn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding2)

        start_btn = findViewById(R.id.start_btn)
        start_btn.setOnClickListener{
            val myIntent = Intent(this, Register::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(myIntent)
        }
    }
}