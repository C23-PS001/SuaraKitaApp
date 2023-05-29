package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: List<UserItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class UserItem(

	@field:SerializedName("nik")
	val nik: Long,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("tanggalLahir")
	val tanggalLahir: String,

	@field:SerializedName("email")
	val email: String
)
