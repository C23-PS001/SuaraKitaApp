package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class VoteResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
