package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName

data class TrainingResponse(

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("message")
	val message: String
)

data class PredictResponse(

	@field:SerializedName("hasilPredict")
	val hasilPredict: String,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("message")
	val message: String
)
