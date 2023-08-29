package com.anlmk.base.di

import com.anlmk.base.data.impl.Service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import com.anlmk.base.BuildConfig
import com.anlmk.base.data.domain.Domain
import retrofit2.converter.gson.GsonConverterFactory


val networks = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), Domain.getDomain("test")) }
    single { provideApiService(get()) }
}


private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()


fun createGson(): Gson {
    return GsonBuilder()
        .setLenient()
        .disableHtmlEscaping()
        .create()
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideApiService(retrofit: Retrofit): Service =
    retrofit.create(Service::class.java)

