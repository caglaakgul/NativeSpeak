package nativespeak.app.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nativespeak.app.base.viewmodel.BaseViewModel
import nativespeak.app.data.UserData
import nativespeak.app.data.response.CountryResponse
import nativespeak.app.data.response.UpdateResponse
import nativespeak.app.remote.ApiService
import nativespeak.app.ui.register.RegisterViewModel
import nativespeak.app.util.PrefUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val apiService: ApiService, private val prefUtil: PrefUtil): BaseViewModel() {
    val liveData = MutableLiveData<SettingsViewModel.State>()

    var countryList = ArrayList<CountryResponse>()
    var selectedCountry = CountryResponse()

    var myUserId: String? = null
    var myCountryCode: String? = null
    var username = MutableLiveData("")

    init{
        this.myUserId = prefUtil.getId()
        this.myCountryCode = prefUtil.getCountryCode()
        this.username.value = prefUtil.getUsername()
    }

    fun getCountry(){
        apiService.getCountries().enqueue(object : Callback<ArrayList<CountryResponse>>{
            override fun onFailure(call: Call<ArrayList<CountryResponse>>, t: Throwable) {}

            override fun onResponse(call: Call<ArrayList<CountryResponse>>, response: Response<ArrayList<CountryResponse>>) {
                countryList = response.body() ?: arrayListOf()
                liveData.value = State.OnUpdateCountryResponse
            }
        })
    }

    fun updateData(newUsername:String, password: String) {
        apiService.updateData(username.value?:"",newUsername,password,selectedCountry.id.toString()).enqueue(object : Callback<UpdateResponse>{
                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {}

            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                Log.i("UPDATE_RESPONSE", "Update response: " + response.body()?.success)
                liveData.value = State.OnSaveUpdate(response.body()?.success?:false)
            }

        })
    }
    sealed class State{
        object OnUpdateCountryResponse : State()
        data class OnSaveUpdate(val success: Boolean) : State()
    }

}