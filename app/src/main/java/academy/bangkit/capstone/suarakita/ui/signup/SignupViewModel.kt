package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.FileUploadResponse
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

    private val _faceResponse = MutableLiveData<Boolean>()
    val faceResponse: MutableLiveData<Boolean> = _faceResponse

    private val _responseKtp = MutableLiveData<String?>()
    val responseKtp: MutableLiveData<String?> = _responseKtp

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
    fun uploadId(imageMultipart: MultipartBody.Part){
        _isLoading.value = true
        val service = ApiConfig.getApiService().uploadId(imageMultipart)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d("KTP Upload", "onResponse: ${responseBody.message}")
                        _responseKtp.value = responseBody.message
                    }
                } else {
                    Log.d("KTP Upload", "onResponse: ${response.message()}")
                    _responseKtp.value = response.message()
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("KTP Upload", "onResponse: ${t.message.toString()}")
            }
        })
    }



    fun hashNik(nik: String): String {
        val hashNik = MessageDigest.getInstance("SHA-256").digest(nik.toByteArray())
        return hashNik.fold("") { str, it -> str + "%02x".format(it) }
    }

}