package com.uzair.composeBase.domain.remote.ship_service

import com.uzair.composeBase.data.room_database.ships.ShipsModel
import retrofit2.Response
import retrofit2.http.GET

interface ShipService {

    @GET("v3/ships")
    suspend fun getShips(): Response<MutableList<ShipsModel>>
}