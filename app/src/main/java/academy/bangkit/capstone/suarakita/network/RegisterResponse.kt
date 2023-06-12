package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("idUser")
	val idUser: String,

)
