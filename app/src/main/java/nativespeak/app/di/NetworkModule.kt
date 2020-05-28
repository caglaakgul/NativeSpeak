package nativespeak.app.di

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import nativespeak.app.BuildConfig
import nativespeak.app.di.scopes.PerApplication
import nativespeak.app.remote.ApiService
import nativespeak.app.remote.TranslateService
import nativespeak.app.util.PrefUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @PerApplication
    fun provideOkHttpClient(application:Application): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            okhttp.addNetworkInterceptor(StethoInterceptor())
            okhttp.addInterceptor(ChuckInterceptor(application))
        }

        return okhttp.build()
    }

    @Provides
    @PerApplication
    fun provideApi(client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @PerApplication
    fun provideTranslateApi(client: OkHttpClient): TranslateService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://translate.yandex.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateService::class.java)
    }

    @Provides
    @PerApplication
    fun providePrefUtil(app:Application): PrefUtil{
        return PrefUtil(app)
    }
}