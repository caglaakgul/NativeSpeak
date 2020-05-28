package nativespeak.app.remote

import nativespeak.app.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("getCountryList.php")
    fun getCountries(): Call<ArrayList<CountryResponse>>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("username") username: String, @Field("password") password: String,
        @Field("country") country: Int, @Field("firebaseToken") firebaseToken: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("firebaseToken") firebaseToken: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("findUser.php")
    fun findUser(@Field("username") username: String): Call<DiscoverResponse>

    @FormUrlEncoded
    @POST("sendMessage.php")
    fun sendMessage(@Field("message") message:String, @Field("translateMessage") translateMessage:String,
                    @Field("userId") userId: String, @Field("senderUserId") senderUserId: String,
                    @Field("firebaseToken") firebaseToken: String
    ): Call<MessageResponse>

    @GET("getMessageList.php")
    fun getMessages(@Query("senderId") senderId: String, @Query("receiverId") receiverId: String): Call<ArrayList<MessageListResponse>>

    @FormUrlEncoded
    @POST("updateData.php")
    fun updateData(@Field("oldUsername") oldUsername: String, @Field("newUsername") newUsername: String, @Field("password") password: String,
                    @Field("country") country: String): Call<UpdateResponse>
}
