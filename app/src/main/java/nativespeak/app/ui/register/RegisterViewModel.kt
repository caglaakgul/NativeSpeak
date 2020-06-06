package nativespeak.app.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import nativespeak.app.base.viewmodel.BaseViewModel
import nativespeak.app.data.response.CountryResponse
import nativespeak.app.data.response.LoginResponse
import nativespeak.app.remote.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val apiService: ApiService) : BaseViewModel() {
    val liveData = MutableLiveData<State>()
    var countryList = ArrayList<CountryResponse>()

    var selectedCountryId = 0

    fun getCountries() {
        apiService.getCountries().enqueue(object : Callback<ArrayList<CountryResponse>> {
            override fun onFailure(call: Call<ArrayList<CountryResponse>>, t: Throwable) {}
            override fun onResponse(call: Call<ArrayList<CountryResponse>>, response: Response<ArrayList<CountryResponse>>) {
                countryList = response.body() ?: arrayListOf()
                liveData.value = State.OnCountryListResponse
            }
        })
    }

    fun register(username: String, password: String, token:String) {
        apiService.register(username, password, selectedCountryId, token).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {}
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.i("TEST_RESPONSE", "response: " + response.body()?.success)
                liveData.value = State.OnRegisterResponse(username, password)
            }
        })
    }

    sealed class State {
        object OnCountryListResponse : State()
        data class OnRegisterResponse(val username: String, val password: String) : State()
    }
}