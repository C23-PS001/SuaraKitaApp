package academy.bangkit.capstone.suarakita.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiService{
    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("nama") nama: String,
        @Field("nik") nik: Long,
        @Field("tanggal") tanggal: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("user/{id}")
    fun getData(
        @Path("id") token: String
    ): Call<UserResponse>
}