package nativespeak.app.ui.login

import androidx.lifecycle.MutableLiveData
import nativespeak.app.base.viewmodel.BaseViewModel
import nativespeak.app.data.UserData
import nativespeak.app.data.response.LoginResponse
import nativespeak.app.remote.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val apiService: ApiService) : BaseViewModel() {
    val liveData = MutableLiveData<State>()

    fun login(username: String, password: String, token: String) {
        apiService.login(username, password, token).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {}

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                liveData.value = State.OnLoginResponse(response.body()?.success?:false, response.body()?.data)
            }
        })
    }

sealed class State {
    data class OnLoginResponse(val success:Boolean, val data: UserData?):State()
}
}