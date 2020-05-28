package nativespeak.app.data.response

import com.google.gson.annotations.SerializedName

data class UpdateResponse(
    @SerializedName("success")
    val success : Boolean
)