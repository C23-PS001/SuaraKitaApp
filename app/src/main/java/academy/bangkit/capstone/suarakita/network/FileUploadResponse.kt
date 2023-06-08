package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
