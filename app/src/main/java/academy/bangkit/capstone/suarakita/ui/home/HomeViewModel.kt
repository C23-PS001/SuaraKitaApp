package academy.bangkit.capstone.suarakita.ui.home

import academy.bangkit.capstone.suarakita.model.UserModel
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.CountResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: UserPreference) : ViewModel() {

    private val _response = MutableLiveData<CountResponse>()
    val response: MutableLiveData<CountResponse> = _response

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getVoteCount(){
        val service = ApiConfig.getApiService().getVoteCount()
        service.enqueue(object : Callback<CountResponse> {
            override fun onResponse(
                call: Call<CountResponse>,
                response: Response<CountResponse>
            ) {
                if (response.isSuccessful) {
                    _response.value = response.body()
                    Log.d("Vote", "onResponse: ${response.body()}")
                } else {
                    Log.d("Vote", "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CountResponse>, t: Throwable) {
                Log.d("Vote", "Gagal dapat vote: ${t.message.toString()}")
            }
        })
    }
}