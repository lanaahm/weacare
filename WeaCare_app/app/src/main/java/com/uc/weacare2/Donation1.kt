package com.uc.weacare2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.uc.weacare2.helper.PreferencesHelper
import com.uc.weacare2.helper.SharedPreference
import com.uc.weacare2.model.donation.DonationCount
import com.uc.weacare2.retrofit.AppModule
import kotlinx.android.synthetic.main.activity_donation1.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Donation1 : Fragment(), View.OnClickListener {
    private lateinit var sph : SharedPreference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.activity_donation1, container, false)
        view.donate_btn.setOnClickListener(this)
        sph = SharedPreference(requireActivity())
        view.profilename.text = sph.getString(PreferencesHelper.PREF_USERNAME)


        AppModule.endpoint.donationCount()
            .enqueue(object : Callback<DonationCount> {
                override fun onResponse(call: Call<DonationCount>, response: Response<DonationCount>) {
                    if (response != null && response.code() == 200){
                        view.totaldonate.text = ""+response.body()!!.data!!.get(0)!!.userDonation
                    }
                }
                override fun onFailure(call: Call<DonationCount>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            })

        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.donate_btn -> {
                val transaction = fragmentManager?.beginTransaction()
                val frag2 = Donation2()

                transaction?.replace(R.id.v_fragment, frag2)
                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction?.commit()
            }
        }
    }
}