package com.example.parquerufinotamayo.model.repository

import android.content.Context
import com.example.parquerufinotamayo.LoginUtils.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepository {
    companion object {
        private lateinit var client: OkHttpClient
        private lateinit var  retrofitInstance: Retrofit

        fun updateRemoteReferences(token: String, context: Context){
            client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()

            retrofitInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun getClient(token: String): OkHttpClient{
            if(!::client.isInitialized){
                client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(token))
                    .build()
            }
            return client
        }

        fun getRetrofitInstance(token: String): Retrofit{
            getClient(token)
            if(!::retrofitInstance.isInitialized){
                retrofitInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofitInstance
        }
    }

    class AuthInterceptor(val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization", "Bearer $token")
            return chain.proceed(requestBuilder.build())
        }
    }
}