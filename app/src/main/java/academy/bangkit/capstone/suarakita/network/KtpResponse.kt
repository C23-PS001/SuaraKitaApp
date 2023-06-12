package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class KtpResponse(

	@field:SerializedName("NIK")
	val nik: String,

	@field:SerializedName("Nama")
	val nama: String,

	@field:SerializedName("Tgl Lahir")
	val tglLahir: String
)
