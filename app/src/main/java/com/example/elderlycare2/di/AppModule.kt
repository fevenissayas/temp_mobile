package com.example.elderlycare2.di

import android.content.Context
import com.example.elderlycare2.data.api.CareScheduleApiService
import com.example.elderlycare2.data.api.LoginApiService
import com.example.elderlycare2.data.api.NurseApiService
import com.example.elderlycare2.data.api.NurseProfileApiService
import com.example.elderlycare2.data.api.ScheduleApi
import com.example.elderlycare2.data.api.SignUpApiService
import com.example.elderlycare2.data.api.UserProfileApi
import com.example.elderlycare2.data.api.VisitDetailsApiService
import com.example.elderlycare2.data.repository.CareScheduleRepository
import com.example.elderlycare2.data.repository.LoginRepository
import com.example.elderlycare2.data.repository.NurseDeleteRepository
import com.example.elderlycare2.data.repository.NurseProfileRespository
import com.example.elderlycare2.data.repository.NurseRepository
import com.example.elderlycare2.data.repository.ScheduleRepository
import com.example.elderlycare2.data.repository.SignUpRepository
import com.example.elderlycare2.data.repository.UserProfileRepository
import com.example.elderlycare2.data.repository.ViewDetailRepository
import com.example.elderlycare2.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(signUpApiService: SignUpApiService): SignUpRepository {
        return SignUpRepository(signUpApiService)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(loginApiService: LoginApiService): LoginRepository {
        return LoginRepository(loginApiService)
    }

    @Provides
    @Singleton
    fun provideNurseRepository(nurseApiService: NurseApiService): NurseRepository {
        return NurseRepository(nurseApiService)
    }

    @Provides
    @Singleton
    fun provideViewDetailRepository(viewDetailsApiService: VisitDetailsApiService): ViewDetailRepository {
        return ViewDetailRepository(viewDetailsApiService)
    }

    @Provides
    @Singleton
    fun provideNurseProfileRepository(nurseProfileApiService: NurseProfileApiService): NurseProfileRespository {
        return NurseProfileRespository(nurseProfileApiService)
    }
    @Provides
    @Singleton
    fun provideCareScheduleRepository(careScheduleApiService: CareScheduleApiService): CareScheduleRepository {
        return CareScheduleRepository(careScheduleApiService)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(scheduleApi: ScheduleApi): ScheduleRepository {
        return ScheduleRepository(scheduleApi)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(userProfileApi: UserProfileApi): UserProfileRepository {
        return UserProfileRepository(userProfileApi)
    }

    @Provides
    @Singleton
    fun provideNurseDeleteRepository(nurseApiService: NurseApiService): NurseDeleteRepository {
        return NurseDeleteRepository(nurseApiService)
    }
}