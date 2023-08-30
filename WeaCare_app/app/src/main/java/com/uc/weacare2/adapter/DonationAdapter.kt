package com.uc.weacare2.adapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.uc.weacare2.R
import com.uc.weacare2.model.weather.Donation

class DonationAdapter(private val dataSet: List<Donation>) :
    RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgset: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            imgset = view.findViewById(R.id.wind_img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}