package com.uc.weacare2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.uc.weacare2.helper.PreferencesHelper
import com.uc.weacare2.helper.SharedPreference
import com.uc.weacare2.model.user.UserLogin
import com.uc.weacare2.model.user.UserRegister
import com.uc.weacare2.retrofit.AppModule
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sph = SharedPreference(this)
        btn_signin.setOnClickListener{

            var username = username_txtinput.editText?.text.toString().trim()
            var password = pw_txtinput.editText?.text.toString().trim()

            AppModule.endpoint.userLogin(username, password)
                .enqueue(object : Callback<UserLogin> {
                    override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                        if (response != null && response.code() == 200){
                            val response = response.body()
                            saveSession(response!!.id, response.username, response.email )
                            Toast.makeText(this@Login, response!!.username, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Login, HomeNavigation::class.java))
                        }
                    }
                    override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                        Toast.makeText(this@Login, t.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }

        regist_txt.setOnClickListener{
            val myIntent = Intent(this, Register::class.java)
            startActivity(myIntent)
        }
    }


    private fun saveSession(id: String?, username: String?, email: String?){
        sph.put( PreferencesHelper.PREF_ID, id!!)
        sph.put( PreferencesHelper.PREF_USERNAME, username!!)
        sph.put( PreferencesHelper.PREF_EMAIL, email!!)
        sph.put( PreferencesHelper.PREF_IS_LOGIN, true)
    }
}