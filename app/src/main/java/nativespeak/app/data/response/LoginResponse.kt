package nativespeak.app.data.response

import com.google.gson.annotations.SerializedName
import nativespeak.app.data.UserData

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserData?
)