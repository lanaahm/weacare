package com.uc.weacare2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.uc.weacare2.model.donation.DonationAdd
import com.uc.weacare2.retrofit.AppModule
import kotlinx.android.synthetic.main.activity_donation2.*
import kotlinx.android.synthetic.main.activity_donation2.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Donation2 : Fragment(), View.OnClickListener {
    private lateinit var price10k_btn : AppCompatButton
    private lateinit var price20k_btn : AppCompatButton
    private lateinit var price50k_btn : AppCompatButton
    private lateinit var price100k_btn : AppCompatButton
    private lateinit var radioGroup: RadioGroup

    private var nominal: Number = 0;
    private var payment_method: String = "";

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_donation2, container, false)

        price10k_btn = view.findViewById(R.id.price10k_btn)
        price20k_btn = view.findViewById(R.id.price20k_btn)
        price50k_btn = view.findViewById(R.id.price50k_btn)
        price100k_btn = view.findViewById(R.id.price100k_btn)
        radioGroup = view.findViewById(R.id.radioGroup)

        price10k_btn.setOnClickListener(this)
        price20k_btn.setOnClickListener(this)
        price50k_btn.setOnClickListener(this)
        price100k_btn.setOnClickListener(this)


        view.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton -> { payment_method = "BCA" }
                R.id.radioButton2 -> { payment_method = "GoPay" }
                R.id.radioButton3 -> { payment_method = "OVO" }
                R.id.radioButton4 -> { payment_method = "Shopeepay" }
            }
        })

        view.button5.setOnClickListener{
            if (nominal == 0) Toast.makeText(context, "Nominal is Required", Toast.LENGTH_SHORT).show()
            else if(payment_method.isEmpty()) Toast.makeText(context, "Payment Method is Required", Toast.LENGTH_SHORT).show()
            else
                AppModule.endpoint.donationAdd(payment_method, nominal.toString())
                    .enqueue(object : Callback<DonationAdd> {
                        override fun onResponse(call: Call<DonationAdd>, response: Response<DonationAdd>) {
                            if (response != null && response.code() == 200){
                                val transaction = fragmentManager?.beginTransaction()
                                val frag2 = Donation1()
                                transaction?.replace(R.id.v_fragment, frag2)
                                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                transaction?.commit()
                            }
                        }
                        override fun onFailure(call: Call<DonationAdd>, t: Throwable) {
                            Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
        }

        return view
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.price10k_btn -> {
                price10k_btn.setBackgroundColor(resources.getColor(R.color.atmosphere));
                price20k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price50k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price100k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                nominal = 10000;
            }
            R.id.price20k_btn -> {
                price10k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price20k_btn.setBackgroundColor(resources.getColor(R.color.atmosphere));
                price50k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price100k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                nominal = 20000;
            }
            R.id.price50k_btn -> {
                price10k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price20k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price50k_btn.setBackgroundColor(resources.getColor(R.color.atmosphere));
                price100k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                nominal = 50000;
            }
            R.id.price100k_btn -> {
                price10k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price20k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price50k_btn.setBackgroundColor(resources.getColor(R.color.thunderstorm));
                price100k_btn.setBackgroundColor(resources.getColor(R.color.atmosphere));
                nominal = 100000;
            }
            R.id.radioButton -> { payment_method = "BCA" }
            R.id.radioButton2 -> { payment_method = "GoPay" }
            R.id.radioButton3 -> { payment_method = "OVO" }
            R.id.radioButton4 -> { payment_method = "Shopeepay" }
        }
    }
}