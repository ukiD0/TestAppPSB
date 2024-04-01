package com.example.psbtesttask.repositroy

/**
 * The starting point for retrofit client.
 * Set the base link to the server and set instructions for interaction.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
object Common {
    private val BASE_URL = "https://www.cbr-xml-daily.ru/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}