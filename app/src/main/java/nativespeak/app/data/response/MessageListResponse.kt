package nativespeak.app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageListResponse(
    @SerializedName("id")
    val id: String ?= null,
    @SerializedName("message_text")
    val message_text: String?= null,
    @SerializedName("translate_text")
    val translate_text: String? = null,
    @SerializedName("userId")
    val userId: String ?= null,
    @SerializedName("senderUserId")
    val senderUserId: String ?= null
):Parcelable