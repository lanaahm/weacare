package com.uc.weacare2.model.donation

import com.google.gson.annotations.SerializedName

data class DonationCount(

	@field:SerializedName("data")
	val data: List<Data?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
){
	data class Data(
		@field:SerializedName("user_donation")
		val userDonation: Int? = null
	)
}

