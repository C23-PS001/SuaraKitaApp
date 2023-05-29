package academy.bangkit.capstone.suarakita.ui.login

import academy.bangkit.capstone.suarakita.model.UserModel
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.LoginResponse
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel(){
    private val _response = MutableLiveData<LoginResponse?>()
    val response: MutableLiveData<LoginResponse?> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val service = ApiConfig.getApiService().login(email, password)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d("Login", "onResponse: ${responseBody.message}")
                        _response.value = responseBody
                    }

                } else {
                    Log.d("Login", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("Login", "Gagal Login: ${t.message.toString()}")
            }
        })
    }
}