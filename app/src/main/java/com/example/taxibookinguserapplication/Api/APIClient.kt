package com.example.taxibookinguserapplication.Api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {

    companion object {

        private var retrofit: Retrofit? = null

        fun getApiClient(BASE_URL: String?): Retrofit? {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            if (retrofit == null) {
                val gson = GsonBuilder().setLenient().create()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL!!)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }



    }


}