package academy.bangkit.capstone.suarakita.network

import okhttp3.MultipartBody
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
        @Field("nik") nik: String,
        @Field("tanggal") tanggal: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("user/{id}")
    fun getData(
        @Path("id") token: String
    ): Call<UserResponse>

    @GET("voting")
    fun getVoteCount(): Call<CountResponse>

    @FormUrlEncoded
    @POST("voting")
    fun vote(
        @Field("idUser") idUser: String,
        @Field("candNum") candNum: Int,
    ): Call<VoteResponse>

    @Multipart
    @POST("test/upload")
    fun uploadId(
        @Part file: MultipartBody.Part,
    ): Call<FileUploadResponse>
}