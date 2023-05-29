package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
