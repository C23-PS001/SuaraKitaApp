package academy.bangkit.capstone.suarakita.ui.vote

import academy.bangkit.capstone.suarakita.model.UserModel
import academy.bangkit.capstone.suarakita.model.UserPreference
import academy.bangkit.capstone.suarakita.network.ApiConfig
import academy.bangkit.capstone.suarakita.network.VoteResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoteViewModel(private val pref: UserPreference) : ViewModel() {
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
}