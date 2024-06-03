package com.currencyconvertor.di

import android.content.Context
import com.currencyconvertor.BuildConfig
import com.currencyconvertor.data.remote.CurrencyConversionApi
import com.currencyconvertor.data.remote.interceptor.NoConnectionInterceptor
import com.currencyconvertor.utils.AppConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()

        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(NoConnectionInterceptor(context))
            .connectTimeout(AppConstants.APIConfig.TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .readTimeout(AppConstants.APIConfig.TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.APIConfig.TIMEOUT_DEFAULT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context,
        moshi: Moshi,
        httpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConstants.APIConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideApi(
        retrofit: Retrofit,
    ): CurrencyConversionApi {
        return retrofit.create(CurrencyConversionApi::class.java)
    }

}