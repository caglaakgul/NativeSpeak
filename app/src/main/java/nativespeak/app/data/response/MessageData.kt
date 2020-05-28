package nativespeak.app.data.response

import com.google.gson.annotations.SerializedName

data class MessageData(
    @SerializedName("code")
    val code: String? = null,

    @SerializedName("lang")
    val lang: String? = null,

    @SerializedName("text")
    val text: ArrayList<String>? = null
)