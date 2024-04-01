package com.example.psbtesttask.repositroy

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/**
 * This object creates, configures and returns a client retrofit
 * for further interaction with the server.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
object RetrofitClient {
    private var retrofit: Retrofit? = null
    /**
     * This function returns the client's retrofit.
     *
     * @param baseUrl Base URL link
     */
    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}