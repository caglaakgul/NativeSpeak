package nativespeak.app.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import nativespeak.app.App
import nativespeak.app.data.UserData
import java.net.PasswordAuthentication
import javax.inject.Inject

class PrefUtil @Inject constructor(private val app: Application){

    private val sharedPreferences:SharedPreferences = app.getSharedPreferences("java.nativespeak.app", Context.MODE_PRIVATE)
    private var editor : SharedPreferences.Editor = sharedPreferences.edit()

    private val KEY_USER_ID = "KEY_USER_ID"
    private val KEY_USERNAME = "KEY_USERNAME"
    private val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"
    private val KEY_COUNTRY_CODE = "KEY_COUNTRY_CODE"

    fun login(data: UserData?){
        editor.apply {
            putString(KEY_USER_ID,data?.id)
            putString(KEY_USERNAME,data?.username)
            putString(KEY_FIREBASE_TOKEN, data?.firebaseToken)
            putString(KEY_COUNTRY_CODE, data?.countryCode)
        }.apply()
    }

    fun update(username:String, countryCode:String){
        editor.apply {
            putString(KEY_USERNAME,username)
            putString(KEY_COUNTRY_CODE, countryCode)
        }.apply()
    }



    fun getId():String? = sharedPreferences.getString(KEY_USER_ID,null)
    fun getUsername():String? = sharedPreferences.getString(KEY_USERNAME,null)
    fun getFirebaseToken():String? = sharedPreferences.getString(KEY_FIREBASE_TOKEN,null)
    fun getCountryCode():String? = sharedPreferences.getString(KEY_COUNTRY_CODE,null)
}