package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class CountResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("candidate2")
	val candidate2: Int,

	@field:SerializedName("candidate1")
	val candidate1: Int
)
