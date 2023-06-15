package academy.bangkit.capstone.suarakita.ui.vote

import academy.bangkit.capstone.suarakita.model.UserModel
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.PredictResponse
import academy.bangkit.capstone.suarakita.network.VerifyResponse
import academy.bangkit.capstone.suarakita.network.VoteResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class VoteViewModel(private val pref: UserPreference) : ViewModel() {

    private val _voteStatus = MutableLiveData<VerifyResponse>()
    var voteStatus: LiveData<VerifyResponse> = _voteStatus

    private val _faceResponse = MutableLiveData<PredictResponse?>()
    val faceResponse: LiveData<PredictResponse?> = _faceResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun setVote(id: String, candNum: Int) {
        val service = ApiConfig.getApiService().vote(id, candNum)
        service.enqueue(object : Callback<VoteResponse> {
            override fun onResponse(
                call: Call<VoteResponse>,
                response: Response<VoteResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.error == false) {
                        Log.d("Vote", "onResponse: ${responseBody.message}")
                    }
                } else {
                    Log.d("Vote", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<VoteResponse>, t: Throwable) {
                Log.d("Vote", "Gagal Login: ${t.message.toString()}")
            }
        })
    }

    fun getVoteStatus(NIK: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().getVoteStatus(NIK)
        service.enqueue(object : Callback<VerifyResponse> {
            override fun onResponse(
                call: Call<VerifyResponse>,
                response: Response<VerifyResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _voteStatus.value = response.body()
                    Log.d("Vote Status", "onResponse: ${response.body()}")
                } else {
                    Log.d("Vote Status", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<VerifyResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Vote Status", "Gagal dapat vote: ${t.message.toString()}")
            }
        })
    }

    fun verifikasiWajah(imageMultipart: MultipartBody.Part, idUser : RequestBody){
        _isLoading.value = true
        val service = ApiConfig.getSiameseService().siamesePredict(imageMultipart, idUser)
        service.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("KTP Upload Berhasil", "onResponse: $responseBody")
                        _faceResponse.value = responseBody
                    }
                } else {
                    _faceResponse.value = null
                    Log.d("Training Wajah Gagal", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Training Wajah Failure", "onResponse: ${t.message.toString()}")
            }
        })
    }

    fun hashNik(nik: String): String {
        val hashNik = MessageDigest.getInstance("SHA-256").digest(nik.toByteArray())
        return hashNik.fold("") { str, it -> str + "%02x".format(it) }
    }
}