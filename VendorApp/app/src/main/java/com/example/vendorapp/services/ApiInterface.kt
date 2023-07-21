package com.example.vendorapp.services

import androidx.annotation.NonNull
import com.example.vendorapp.modelclass.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("Auth/GenerateOtp")
    fun Gen_otp(
        @Field("mobile_number") mobile_number: String,
    ): Call<Mobileotp_Response>


    @FormUrlEncoded
    @POST("Auth/VerifyOtp")
    fun Verify_otp(
        @Field("mobile_number") mobile_number: String,
        @Field("otp") otp: String,
    ): Call<Verify_otp_Response>





    @FormUrlEncoded
    @POST("Home/wallet")
    fun WalletDetailsInterface(
        @Header("Sessionid") content_type:String?,
        @Field("otp") otp: String,
    ): Call<WalletModal>




    @Multipart
    @NonNull
    @POST("Auth/create_vendor_user")
    fun UploadBusinessRegFilesInterface(
        @HeaderMap headers: Map<String, String>,
        @Part("type") type: RequestBody,
        @Part image1: MultipartBody.Part,
    ): Call<Verify_otp_Response>



    @Multipart
    @NonNull
    @POST("Auth/create_vendor_user")
    fun businessdetails(
       //@Header("Sessionid")  content_type:String?,
        @Part("type") type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("business_name") business_name: RequestBody,
        @Part("mobile")contact_mob_num: RequestBody,
        @Part("email")store_email_id: RequestBody,
        @Part("category_id")business_category: RequestBody,
        @Part("address")address: RequestBody,
        @Part("location")location: RequestBody,
        @Part image: MultipartBody.Part
        ): Call<Verify_otp_Response>

    @FormUrlEncoded
    @POST("Home/profile_details_update")
    fun bankaccountdetails(
       @Header("Sessionid") content_type: String?,
//        @Field("type") type: RequestBody,
        @Field("ifsc") bank_name: String,
        @Field("bank_name") ifsc_code: String,
        @Field("account_number") account_number: String,
    ): Call<Bankdetails_Response>



    @FormUrlEncoded
    @POST("Home/contact_details")
    fun contactdetails(
      @Header("Sessionid")  content_type:String?,
        @Field("name") emergency_contact_name: String,
        @Field("mobile_number") emergency_mobile_number: String,
    ): Call<Bankdetails_Response>

    @POST("Home/Dashboard") fun Dashboarddetails(@Header("Sessionid") Sessionid: String?): Call<DashboardResponseModal>



}