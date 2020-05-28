package nativespeak.app.data.response

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("code") val code: String? = null
)
