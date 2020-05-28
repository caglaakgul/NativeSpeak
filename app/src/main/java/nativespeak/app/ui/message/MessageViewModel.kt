package nativespeak.app.ui.message

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import nativespeak.app.BuildConfig
import nativespeak.app.base.viewmodel.BaseViewModel
import nativespeak.app.data.UserData
import nativespeak.app.data.response.MessageData
import nativespeak.app.data.response.MessageListResponse
import nativespeak.app.data.response.MessageResponse
import nativespeak.app.remote.ApiService
import nativespeak.app.remote.TranslateService
import nativespeak.app.util.PrefUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val apiService: ApiService, private val translateApiService: TranslateService, private val prefUtil: PrefUtil
) : BaseViewModel() {
    val liveData = MutableLiveData<State>()
    var userData: UserData? = null
    var myUserId: String? = null
    var myCountryCode: String? = null

    var messageList: ArrayList<MessageListResponse>? = null

    var username = MutableLiveData("Guest")
    var hisUsername = MutableLiveData("")

    init {
        //this.myUserData = UserData(prefUtil.getId(), prefUtil.getUsername(), prefUtil.getFirebaseToken(), prefUtil.getCountryCode())
        this.myUserId = prefUtil.getId()
        this.myCountryCode = prefUtil.getCountryCode()

        this.username.value = prefUtil.getUsername()
    }

    fun setHisUserData(userData: UserData?) {
        this.userData = userData
        this.hisUsername.value = userData?.username
    }

    fun getMessages() {
        apiService.getMessages(myUserId ?: "", userData?.id ?: "").enqueue(object : Callback<ArrayList<MessageListResponse>> {
            override fun onFailure(call: Call<ArrayList<MessageListResponse>>, t: Throwable) {
            }

            override fun onResponse(call: Call<ArrayList<MessageListResponse>>, response: Response<ArrayList<MessageListResponse>>) {
                Log.i("MessageVMTGest", Gson().toJson(response.body()))
                messageList = response.body()
                liveData.value = State.OnMessageListResponse
            }

        })
    }


    fun translateText(message: String) {
        Log.i("messagevmtranslateText", "başladı")
        translateApiService.translateText(BuildConfig.API_KEY, "${myCountryCode}-${userData?.countryCode}", message)
            .enqueue(object : Callback<MessageData> {
                override fun onFailure(call: Call<MessageData>, t: Throwable) {

                }

                override fun onResponse(call: Call<MessageData>, response: Response<MessageData>) {
                    Log.i("messagevmtranslateText", "success")
                    sendMessage(message, response.body()?.text?.get(0))
                }
            })
    }

    fun sendMessage(message: String, translateMessage: String?) {
        Log.i("messagevmsendMessage", "başladı")
        apiService.sendMessage(message, translateMessage ?: "", userData?.id ?: "", myUserId ?: "", userData?.firebaseToken ?: "")
            .enqueue(object : Callback<MessageResponse> {
                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    if (response.isSuccessful)
                        liveData.value = State.OnSendMessageSuccess(response.body()?.data)
                }
            })
    }


    sealed class State {
        object OnMessageListResponse : State()
        data class OnSendMessageSuccess(val message: MessageListResponse?) : State()
        //data class OnError(val errorMessage: String) : State()
    }
}