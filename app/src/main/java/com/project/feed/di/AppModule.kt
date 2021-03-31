package com.project.feed.di

import android.content.Context
import com.project.feed.*
import com.project.feed.data.repository.DataRepository
import com.project.feed.network.RemoteDataSource
import com.project.feed.network.RetrofitService
import com.project.feed.network.connectivity.base.ConnectivityProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .build()
    }

    @Provides
    fun provideCache(@ApplicationContext appContext: Context): Cache {
        val httpCacheDirectory = File(appContext.cacheDir, CACHE_DIR_CHILD)
        return Cache(httpCacheDirectory, CACHE_SIZE)
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(retrofitService: RetrofitService) =
        RemoteDataSource(retrofitService)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource) = DataRepository(remoteDataSource)

    @Singleton
    @Provides
    fun providesNetworkConnectivity(@ApplicationContext appContext: Context): ConnectivityProvider =
        ConnectivityProvider.createProvider(appContext)
}