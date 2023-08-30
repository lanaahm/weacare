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
import kotlinx.android.synthetic.main.activity_donation1.view.*
import kotlinx.android.synthetic.main.activity_profile.view.*

class ProfileActivity : Fragment() {
    private lateinit var sph : SharedPreference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        sph = SharedPreference(requireActivity())
        view.textView16.text = sph.getString(PreferencesHelper.PREF_USERNAME)
        view.textView17.text = sph.getString(PreferencesHelper.PREF_EMAIL)

        view.textView18.setOnClickListener{
            sph.clear()
            Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, Login::class.java))
        }
        view.textView4.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            val frag2 = EditProfileActivity()

            transaction?.replace(R.id.v_fragment, frag2)
            transaction?.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction?.commit()
        }

        return view
    }
}