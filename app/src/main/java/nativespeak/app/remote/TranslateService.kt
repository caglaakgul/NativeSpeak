package nativespeak.app.remote

import nativespeak.app.data.response.MessageData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateService {
    @GET("api/v1.5/tr.json/translate")
    fun translateText(
        @Query("key") key: String,
        @Query("lang") lang: String,
        @Query("text") text: String
    ): Call<MessageData>
}