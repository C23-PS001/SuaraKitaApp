package academy.bangkit.capstone.suarakita.ui.profile

import academy.bangkit.capstone.suarakita.model.UserModel
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.UserItem
import academy.bangkit.capstone.suarakita.network.UserResponse
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (private val pref: UserPreference) : ViewModel(){
    private val _response = MutableLiveData<List<UserItem>>()
    val response: MutableLiveData<List<UserItem>> = _response

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getProfile(token:String){
        val service = ApiConfig.getApiService().getData(token)
        service.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    _response.value = response.body()?.data
                    Log.d("Profile", "onResponse: ${response.body()}")
                } else {
                    Log.d("Profile", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Profile", "Gagal Login: ${t.message.toString()}")
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}