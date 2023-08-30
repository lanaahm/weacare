package com.uc.weacare2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.uc.weacare2.helper.PreferencesHelper
import com.uc.weacare2.helper.SharedPreference
import com.uc.weacare2.model.User
import com.uc.weacare2.model.user.UserRegister
import com.uc.weacare2.retrofit.AppModule
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private lateinit var sph : SharedPreference
    private lateinit var user: User
    private var position = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sph = SharedPreference(this)
        setContentView(R.layout.activity_register)
        listener()
    }
    private fun listener(){
        signup_btn.setOnClickListener {
            var username = username_textinput.editText?.text.toString().trim()
            var email = email_textinput.editText?.text.toString().trim()
            var password = password_textinput.editText?.text.toString().trim()

            user = User(username,email,password)
            checker()

        }

        signIn_txt.setOnClickListener{
            val myIntent = Intent(this, Login::class.java)
            startActivity(myIntent)
        }
    }

    private fun checker(){
        var isCompleted:Boolean = true

        //cek nama
        if(user.nama!!.isEmpty()){
            username_textinput.error = "Tolong isi kolom nama"
            isCompleted = false
        }else{
            username_textinput.error = ""
        }
        //cek email
        if(user.email!!.isEmpty()){
            email_textinput.error = "Tolong isi kolom email"
            isCompleted= false
        }else{
            //berguna untuk cek apakah input merupakan email
            if (!Patterns.EMAIL_ADDRESS.matcher(user.email!!).matches()){
                email_textinput.error = "Tolong masukkan alamat email yang benar!"
                isCompleted = false
            }else {
                email_textinput.error = ""
            }
        }

        //cek password
        if(user.password!!.isEmpty()){
            password_textinput.error = "Tolong isi kolom password"
            isCompleted = false
        }else{
            if(user.password!!.length < 8){
                password_textinput.error = "Jumlah password min 8 karakter"
                isCompleted= false
            }else if(!user.password!!.matches(".*[a-z].*".toRegex())) {
                password_textinput.error = "Jassword tidak memiliki huruf kecil"
                isCompleted = false
            }else if(!user.password!!.matches(".*[A-Z].*".toRegex())){
                password_textinput.error = "Password tidak memiliki huruf kapital"
                isCompleted = false
            }else{
                password_textinput.error = ""
            }
        }
        if(isCompleted) {
            if (position == -1) {
                AppModule.endpoint.userRegister(user.nama, user.email, user.password)
                    .enqueue(object : Callback<UserRegister> {
                        override fun onResponse(call: Call<UserRegister>, response: Response<UserRegister>) {
                            val response = response.body()
                            if (response != null && response.status == 200){
                                    Toast.makeText(this@Register, response.message, Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@Register, Login::class.java))
                            }
                        }
                        override fun onFailure(call: Call<UserRegister>, t: Throwable) {
                            Toast.makeText(this@Register, t.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (sph.getBoolean( PreferencesHelper.PREF_IS_LOGIN )) {
            startActivity(Intent(this@Register, HomeNavigation::class.java))
        }
    }
}
