package com.example.elderlycare2.di

import com.example.elderlycare2.data.api.CareScheduleApiService
import com.example.elderlycare2.data.api.LoginApiService
import com.example.elderlycare2.data.api.NurseApiService
import com.example.elderlycare2.data.api.NurseProfileApiService
import com.example.elderlycare2.data.api.ScheduleApi
import com.example.elderlycare2.data.api.SignUpApiService
import com.example.elderlycare2.data.api.UserProfileApi
import com.example.elderlycare2.data.api.VisitDetailsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val BASE_URL = "http://192.168.232.187:8000"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
       // authInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        //.addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSignUpApiService(retrofit: Retrofit): SignUpApiService =
        retrofit.create(SignUpApiService::class.java)

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService =
        retrofit.create(LoginApiService::class.java)

    @Provides
    @Singleton
    fun provideNurseApiService(retrofit: Retrofit): NurseApiService =
        retrofit.create(NurseApiService::class.java)

    @Provides
    @Singleton
    fun provideVisitDetailsApiService(retrofit: Retrofit): VisitDetailsApiService {
        return retrofit.create(VisitDetailsApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideNurseProfileApiService(retrofit: Retrofit): NurseProfileApiService {
        return retrofit.create(NurseProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCareScheduleApiService(retrofit: Retrofit): CareScheduleApiService {
        return retrofit.create(CareScheduleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi {
        return retrofit.create(ScheduleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserProfileApi(retrofit: Retrofit): UserProfileApi {
        return retrofit.create(UserProfileApi::class.java)
    }
}