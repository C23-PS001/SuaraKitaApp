package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class VoteResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class VerifyResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("isVoted")
	val isVoted: Boolean? = null,

	@field:SerializedName("dataExist")
	val dataExist: Boolean? = null
)

data class FaceResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
