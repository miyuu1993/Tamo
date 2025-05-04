package com.example.tamo.data

import retrofit2.http.GET
import retrofit2.http.Query

interface MeigenApi {
    @GET("json.php")
    suspend fun getMeigen(@Query("c") count: Int): List<Meigen>
}
