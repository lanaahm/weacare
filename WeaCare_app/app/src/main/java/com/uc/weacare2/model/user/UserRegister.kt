package com.uc.weacare2.model.user

import com.google.gson.annotations.SerializedName

data class UserRegister(
	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null

) {
	data class Data(
		@field:SerializedName("last_insert_id")
		val lastInsertId: Int? = null
	)
}

