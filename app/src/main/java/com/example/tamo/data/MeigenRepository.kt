package com.example.tamo.data

import javax.inject.Inject

class MeigenRepository @Inject constructor(
    private val meigenApi: MeigenApi
) {
    suspend fun fetchMeigen(): Meigen? {
        return try {
            meigenApi.getMeigen(1).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

}