package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class KtpResponse(

	@field:SerializedName("NIK")
	val nik: String,

	@field:SerializedName("Nama")
	val nama: String,

	@field:SerializedName("Tgl Lahir")
	val tglLahir: String,

	@field:SerializedName("Link Photo")
	val linkFoto: String
)

data class DeleteKtpResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,
)
