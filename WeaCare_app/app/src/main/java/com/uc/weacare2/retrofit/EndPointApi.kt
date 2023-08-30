package com.uc.weacare2.retrofit
import com.uc.weacare2.model.donation.DonationAdd
import com.uc.weacare2.model.donation.DonationCount
import com.uc.weacare2.model.user.UserLogin
import com.uc.weacare2.model.user.UserRegister
import com.uc.weacare2.model.user.UserUpdate
import retrofit2.Call
import retrofit2.http.*

interface EndPointApi {
    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserRegister>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserLogin>

    @FormUrlEncoded
    @PUT("updateuser")
    fun userUpdate(
        @Field("id") id: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserUpdate>

    @GET("/donation/count")
    fun donationCount(): Call<DonationCount>

    @FormUrlEncoded
    @POST("donation")
    fun donationAdd(
        @Field("payment_method") username: String,
        @Field("user_donation") password: String
    ): Call<DonationAdd>
}

