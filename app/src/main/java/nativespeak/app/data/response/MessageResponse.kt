package nativespeak.app.data.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("data") val data: MessageListResponse?
)