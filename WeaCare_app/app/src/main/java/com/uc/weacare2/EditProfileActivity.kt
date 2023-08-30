package com.uc.weacare2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.uc.weacare2.helper.PreferencesHelper
import com.uc.weacare2.helper.SharedPreference
import com.uc.weacare2.model.user.UserRegister
import com.uc.weacare2.model.user.UserUpdate
import com.uc.weacare2.retrofit.AppModule
import kotlinx.android.synthetic.main.activity_donation1.view.*
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : Fragment() {
    private lateinit var sph : SharedPreference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_edit_profile, container, false)

        sph = SharedPreference(requireActivity())
        view.namas_textInputLayout2.editText?.setText(sph.getString(PreferencesHelper.PREF_USERNAME))
        view.emails_textInputLayout3.editText?.setText(sph.getString(PreferencesHelper.PREF_EMAIL))

        view.submits_button2.setOnClickListener{
            val id = sph.getString(PreferencesHelper.PREF_ID)
            val uesrname = view.namas_textInputLayout2.editText!!.text
            val email = view.emails_textInputLayout3.editText!!.text
            val password = view.password_textInputLayout4.editText!!.text

            if (uesrname.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(context, "Field Tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{

                AppModule.endpoint.userUpdate(id!!, uesrname.toString(), email.toString(), password.toString())
                    .enqueue(object : Callback<UserUpdate> {
                        override fun onResponse(call: Call<UserUpdate>, response: Response<UserUpdate>) {
                            val response = response.body()
                            if (response != null && response.status == 200){
                                saveSession(id!!, uesrname.toString(), email.toString())
                                Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()

                                val transaction = fragmentManager?.beginTransaction()
                                val frag2 = ProfileActivity()

                                transaction?.replace(R.id.v_fragment, frag2)
                                transaction?.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                transaction?.commit()
                            }
                        }
                        override fun onFailure(call: Call<UserUpdate>, t: Throwable) {
                            Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }


        view.imageView9.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            val frag2 = ProfileActivity()

            transaction?.replace(R.id.v_fragment, frag2)
            transaction?.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction?.commit()
        }

        return view
    }
    private fun saveSession(id: String?, username: String?, email: String?){
        sph.put( PreferencesHelper.PREF_ID, id!!)
        sph.put( PreferencesHelper.PREF_USERNAME, username!!)
        sph.put( PreferencesHelper.PREF_EMAIL, email!!)
        sph.put( PreferencesHelper.PREF_IS_LOGIN, true)
    }
}