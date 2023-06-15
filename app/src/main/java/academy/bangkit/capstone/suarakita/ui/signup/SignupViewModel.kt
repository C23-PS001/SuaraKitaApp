package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class SignupViewModel(private val pref: UserPreference) : ViewModel(){
    private val _response = MutableLiveData<RegisterResponse?>()
    val response: MutableLiveData<RegisterResponse?> = _response

    private val _faceResponse = MutableLiveData<TrainingResponse?>()
    val faceResponse: MutableLiveData<TrainingResponse?> = _faceResponse

    private val _deleteResponse = MutableLiveData<DeleteKtpResponse?>()
    val deleteResponse: MutableLiveData<DeleteKtpResponse?> = _deleteResponse

    private val _responseKtp = MutableLiveData<KtpResponse?>()
    val responseKtp: MutableLiveData<KtpResponse?> = _responseKtp

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun registerUser(nama: String, nik:String, tanggal:String, email: String, password: String) {
        _isLoading.value = true
        val service = ApiConfig.getApiService().register(nama, nik, tanggal, email,password)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = response.body()
                    Log.d("Register", "onResponse: ${response.body()?.message}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Register", "Gagal Buat Akun: ${t.message.toString()}")}
        })
    }
    fun uploadKtp(imageMultipart: MultipartBody.Part){
        _isLoading.value = true
        val service = ApiConfig.getOcrService().uploadId(imageMultipart)
        service.enqueue(object : Callback<KtpResponse> {
            override fun onResponse(
                call: Call<KtpResponse>,
                response: Response<KtpResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("KTP Upload Berhasil", "onResponse: $responseBody")
                        _responseKtp.value = responseBody
                    }
                } else {
                    Log.d("KTP Upload Gagal1", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<KtpResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("KTP Gagal 2", "onResponse: ${t.message.toString()}")
            }
        })
    }

    fun uploadPhoto(imageMultipart1: MultipartBody.Part, imageMultipart2: MultipartBody.Part, idUser : RequestBody){
        _isLoading.value = true
        Log.d("Face ID User", "uploadPhoto: $idUser")
        val service = ApiConfig.getSiameseService().siameseTraining(imageMultipart1, imageMultipart2, idUser)
        service.enqueue(object : Callback<TrainingResponse> {
            override fun onResponse(
                call: Call<TrainingResponse>,
                response: Response<TrainingResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("Upload Wajah Berhasil", "onResponse: $responseBody")
                        _faceResponse.value = responseBody
                    }
                } else {
                    Log.d("Training Wajah Gagal", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<TrainingResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Training Wajah Failure", "onResponse: ${t.message.toString()}")
            }
        })
    }

    fun deleteKtp(link: String){
        _isLoading.value = true
        val service = ApiConfig.getOcrService().deleteId(link)
        service.enqueue(object : Callback<DeleteKtpResponse> {
            override fun onResponse(
                call: Call<DeleteKtpResponse>,
                response: Response<DeleteKtpResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("KTP Delete Berhasil", "onResponse: $responseBody")
                        _deleteResponse.value = responseBody
                    }
                } else {
                    Log.d("Hapus KTP Gagal", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DeleteKtpResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Server Gagal Hapus KTP", "onResponse: ${t.message.toString()}")
            }
        })
    }

    fun hashNik(nik: String): String {
        val hashNik = MessageDigest.getInstance("SHA-256").digest(nik.toByteArray())
        return hashNik.fold("") { str, it -> str + "%02x".format(it) }
    }

}