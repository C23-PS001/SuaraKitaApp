package academy.bangkit.capstone.suarakita.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Field("tanggalLahir") tanggalLahir: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("user/{id}")
    fun getData(
        @Path("id") token: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("voting")
    fun vote(
        @Field("idUser") idUser: String,
        @Field("candNum") candNum: Int,
    ): Call<VoteResponse>

    @Multipart
    @POST("masuk")
    fun uploadId(
        @Part image: MultipartBody.Part
    ): Call<KtpResponse>

    @FormUrlEncoded
    @POST("keluar")
    fun deleteId(
        @Field ("linkFoto") linkFoto: String
    ): Call<DeleteKtpResponse>

    @GET("voting/checkUser/{nik}")
    fun getVoteStatus(
        @Path("nik") nik: String,
    ): Call<VerifyResponse>

    @Multipart
    @POST("training")
    fun siameseTraining(
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part("idUser") idUser: RequestBody
    ): Call<TrainingResponse>

    @Multipart
    @POST("predict")
    fun siamesePredict(
        @Part Gambar: MultipartBody.Part,
        @Part("idUser") idUser: RequestBody
    ): Call<PredictResponse>
}