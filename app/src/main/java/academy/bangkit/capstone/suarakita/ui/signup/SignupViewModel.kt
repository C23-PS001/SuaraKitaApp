package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.FaceResponse
import academy.bangkit.capstone.suarakita.network.KtpResponse
import academy.bangkit.capstone.suarakita.network.RegisterResponse
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class SignupViewModel(private val pref: UserPreference) : ViewModel(){
    private val _response = MutableLiveData<RegisterResponse?>()
    val response: MutableLiveData<RegisterResponse?> = _response

    private val _faceResponse = MutableLiveData<Boolean?>()
    val faceResponse: MutableLiveData<Boolean?> = _faceResponse

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
//                    _responseKtp.value = response.message()
                }
            }
            override fun onFailure(call: Call<KtpResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("KTP Gagal 2", "onResponse: ${t.message.toString()}")
            }
        })
    }

    fun uploadPhoto(imageMultipart1: MultipartBody.Part, imageMultipart2: MultipartBody.Part, idUser : String){
        _isLoading.value = true
        val service = ApiConfig.getSiameseService().uploadFace(imageMultipart1, imageMultipart2, idUser)
        service.enqueue(object : Callback<FaceResponse> {
            override fun onResponse(
                call: Call<FaceResponse>,
                response: Response<FaceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("KTP Upload Berhasil", "onResponse: $responseBody")
                        _faceResponse.value = responseBody.error
                    }
                } else {
                    Log.d("Training Wajah Gagal", "onResponse: ${response.message()}")
//                  _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<FaceResponse>, t: Throwable) {
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