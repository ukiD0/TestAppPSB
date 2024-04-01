package com.example.psbtesttask.repositroy

import com.example.psbtesttask.models.PSBMain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * These are instructions for the retrofit client to interact with the server.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
interface RetrofitServices {
    /**
     * This function is for get current data on currencies.
     */
    @GET("daily_json.js")
    fun getPSBData(): Call<PSBMain>
    /**
     * This function is for get data on currencies for a specific date.
     *
     * @param prevUrl Previous url address.
     */
    @GET("{prevUrl}")
    fun getArchiveData(
        @Path("prevUrl") prevUrl: String
    ):Call<PSBMain>
}