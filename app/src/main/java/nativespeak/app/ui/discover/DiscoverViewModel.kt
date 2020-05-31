package nativespeak.app.ui.discover

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import nativespeak.app.base.viewmodel.BaseViewModel
import nativespeak.app.data.UserData
import nativespeak.app.data.response.DiscoverResponse
import nativespeak.app.remote.ApiService
import nativespeak.app.util.PrefUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DiscoverViewModel @Inject constructor(private val apiService: ApiService, private val prefUtil: PrefUtil) : BaseViewModel() {
    val liveData = MutableLiveData<State>()
    var name = MutableLiveData("Guest")


    fun findUser(username: String) {
        apiService.findUser(username).enqueue(object : Callback<DiscoverResponse> {
            override fun onFailure(call: Call<DiscoverResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<DiscoverResponse>, response: Response<DiscoverResponse>) {
                Log.i("test_response", Gson().toJson(response.body()))
                liveData.value = State.OnDiscoverResponse(response.body()?.success, response.body()?.data)
            }

        })
    }

    sealed class State {
        data class OnDiscoverResponse(val success: Boolean?, val data: UserData?) : State()
    }
}