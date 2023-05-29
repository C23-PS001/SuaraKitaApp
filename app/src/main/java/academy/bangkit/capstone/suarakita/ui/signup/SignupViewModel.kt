package academy.bangkit.capstone.suarakita.ui.signup

import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.RegisterResponse
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignupViewModel(private val pref: UserPreference) : ViewModel(){
    private val _response = MutableLiveData<String?>()
    val response: MutableLiveData<String?> = _response

    val nama: String = "Bryan Ganteng"
    val nik: Long = 123123123
    val tanggal : String = "2021-06-01"

    fun registerUser(email: String, password: String) {
        val service = ApiConfig.getApiService().register(nama, nik, tanggal, email,password)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d("Register", "onResponse: ${responseBody.message}")
                        _response.value = responseBody.message
                    }
                } else {
                    Log.d("Register", "onResponse: ${response.message()}")
                    _response.value = response.message()
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d("Register", "Gagal Buat Akun: ${t.message.toString()}")
                _response.value = t.message.toString()
            }
        })
    }

}