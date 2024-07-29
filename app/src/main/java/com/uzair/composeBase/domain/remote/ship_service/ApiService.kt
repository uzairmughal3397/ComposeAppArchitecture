package com.uzair.composeBase.domain.remote.ship_service

import com.uzair.composeBase.data.genericModelClasses.ResponseTemplate
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import retrofit2.Response
import retrofit2.http.GET
import javax.xml.transform.Templates

interface ApiService {

    @GET("v3/ships")
    suspend fun getShips(): Response<MutableList<ShipsModel>>

    //for apis related to Vp use ResponseTemplate class
    @GET("address")
    suspend fun getAddress():Response<ResponseTemplate<MutableList<ShipsModel>>>
}