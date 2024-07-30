package com.uzair.composeBase.data.remote

import com.uzair.composeBase.data.local.models.genericModelClasses.ResponseTemplate
import com.uzair.composeBase.data.local.models.ShipsModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("v3/ships")
    suspend fun getShips(): Response<MutableList<ShipsModel>>

    //for apis related to Vp use ResponseTemplate class
    @GET("address")
    suspend fun getAddress():Response<ResponseTemplate<MutableList<ShipsModel>>>
}