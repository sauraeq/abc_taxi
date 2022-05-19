package com.example.taxibookinguserapplication.Api



import com.example.taxibookinguserapplication.Responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

import retrofit2.http.POST


interface APIConfiguration {
    @POST("users/usersignup")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun signup(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<SignUpResponse>

    @POST("users/languagelist")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun languagelist(
      //  @Body stringStringHashMap: HashMap<String, String>,
    ): Call<LanguageRespnse>

     @POST("users/userLoginotp")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun signin(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<SigninResponse>

     @POST("users/loginotpverify")
      @Headers("Content-Type:application/x-www-form-urlencoded")
      fun otpverify(
          @Body stringStringHashMap: HashMap<String, String>,
          ):Call<LoginotpverifyResponse>


    @POST("users/privacypolicy")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun privacy(
    ):Call<Privacy_Response>

    @POST("users/termsconditions")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun TC(
    ):Call<TCResponse>

    @POST("users/usereditprofile")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun edit_profile(
        @Body stringStringHashMap: HashMap<String, String>,
    ):Call<EditProfileResponse>

    @Multipart
    @POST("users/edituser")
    fun profile_img_update(
        @Part("user_id") driver_id: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<EditImgResponse>

    @POST("users/triphistory")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun trip_his(
        @Body stringStringHashMap: HashMap<String, String>,
    ):Call<TripHistory_Response>

    @POST("users/vehiclelist")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun veh_list(
        @Body stringStringHashMap: HashMap<String, String>,
    ):Call<VehicleListResponse>

    @POST("users/booking")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun booking(
        @Body stringStringHashMap: HashMap<String, String>,
    ):Call<BookingResponse>

    @POST("users/bookingstatus")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun booking_status(
        @Body stringStringHashMap: HashMap<String, String>,
    ):Call<BookingStatusResponse>

    @POST("users/cancelreasondata")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun CancelReason(): Call<CancelRideResonResponse>

    @POST("users/cancelride")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun CancelResultSubmission(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<CancelTripResponse>

    @POST("users/addreview")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun rating(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<RatingReviewResponse>

    /*  @POST("user/resend")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun ResendOtp(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<ResendOtpResponse>


     @POST("user/forget")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun foget(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<ForgotPasswordResponse>

     @POST("user/changepassword")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun reset(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<ResetResponse>

     @POST("user/addclient")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun addclient(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<AddClientResponse>

     @POST("user/getClientByLabId")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun Getclient(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<GetClientResponse>

     @POST("lab/getexercise")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun GetExercise(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<GetExerciseResponse>

     @POST("lab/gettemplates")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun GetTemplate(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<GetTemplateResponse>

     @POST("lab/geteducation")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun GetEducation(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<GetEducationResponse>

     @POST("lab/favourite")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun favourite(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<FavouriteResponse>

     @POST("lab/assignLibrary")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun assignlibrary(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<AssignLibraryUser_Response>

     @POST("lab/createtemplate")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun createtemplate(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<CreateTemplateResponse>

     @POST("video_settings")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun videosettings(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<VideoSettingREsponse>

     @POST("settings")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun Notificationsettings(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<SettingResponse>

     @GET("user/country")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun Country(

     ):Call<CountryResponse>

     @POST("user/pages")
     @Headers("Content-Type:application/x-www-form-urlencoded")
     fun termspolicy(
         @Body stringStringHashMap: HashMap<String, String>,
     ):Call<TermsPolicyResponse>



     @Multipart
     @POST("lab/addexercise")
     fun AddExercise(
         @Part("exercise_name") exercise_name: RequestBody,
         @Part("additional_instruction") additional_instruction: RequestBody,
         @Part("client_visible") client_visible: RequestBody,
         @Part("speciality") speciality: RequestBody,
         @Part("required_equipment") required_equipment: RequestBody,
         @Part("repeat") repeat: RequestBody,
         @Part("sets") sets: RequestBody,
         @Part("hold_position") hold_position: RequestBody,
         @Part("use_weight") use_weight: RequestBody,
         @Part("required_duration") required_duration: RequestBody,
         @Part("required_target_rate") required_target_rate: RequestBody,
         @Part("difficulty") difficulty: RequestBody,
         @Part("required_rpe") direquired_rpefficulty: RequestBody,
         @Part("user_id") user_id: RequestBody,
         @Part image: MultipartBody.Part?,
         @Part video: MultipartBody.Part?
     ): Call<AddExerciseResponse>

 @Multipart
 @POST("lab/addeducation")
 fun AddEducation(
     @Part("media_type") media_type:RequestBody,
     @Part("title") title:RequestBody,
     @Part("keyword") keyword:RequestBody,
     @Part("age_group") age_group:RequestBody,
     @Part("joint") joint:RequestBody,
     @Part("mobility") mobility:RequestBody,
     @Part("task") task:RequestBody,
     @Part("user_id") user_id:RequestBody,
     @Part image: MultipartBody.Part?,
     @Part video: MultipartBody.Part?
     ):Call<AddEducationResponse>

     @Multipart
     @POST("update_profile")
     fun Profile(
         @Part("user_id") user_id:RequestBody,
         @Part("first_name") first_name:RequestBody,
         @Part("last_name") last_name:RequestBody,
         @Part("email") email:RequestBody,
         @Part("password") password:RequestBody,
         @Part("mobile") mobile:RequestBody,
         @Part("practics_name") practics_name:RequestBody,
         @Part("address_line1") address_line1:RequestBody,
         @Part("address_line2") address_line2:RequestBody,
         @Part("city") city:RequestBody,
         @Part("postal_code") postal_code:RequestBody,
         @Part("country") country:RequestBody,
         @Part("vat_no") vat_no:RequestBody,
         @Part image: MultipartBody.Part?,
     ):Call<UpdateProfileResponse>
 */

}

