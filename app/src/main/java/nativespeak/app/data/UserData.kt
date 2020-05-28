package nativespeak.app.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserData (
    @SerializedName("id") val id: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("firebaseToken") val firebaseToken: String? = null,
    @SerializedName("countryCode") val countryCode: String? = null,
    @SerializedName("country") val countryId: Int? = null,
    @SerializedName("password") val password: String?= null
):Parcelable

