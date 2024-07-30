package com.uzair.composeBase.data.remote.repository

import com.google.gson.Gson
import com.uzair.composeBase.R
import com.uzair.composeBase.application.MainApplication
import com.uzair.composeBase.data.local.models.genericModelClasses.ResponseError
import com.uzair.composeBase.data.local.db.AppDatabase
import com.uzair.composeBase.data.local.models.ShipsModel
import com.uzair.composeBase.utils.NetworkHelper
import com.uzair.composeBase.data.remote.ApiService
import com.uzair.composeBase.data.remote.Resource
import com.uzair.composeBase.utils.extensions.handleApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private var apiService: ApiService,
    private var appDatabase: AppDatabase,
    private val networkHelper: NetworkHelper
) {

    suspend fun fetchShipsData() =
        handleApiResponse(networkHelper.isNetworkConnected()) {
            apiService.getShips()
        }

    suspend fun saveShipDataIntoDatabase(shipsModelList: MutableList<ShipsModel>) {
        shipsModelList.forEach { shipsModel ->
            appDatabase.shipDao().insertShipModel(shipsModel)
        }
    }

    suspend fun queryToGetAllShips() =
        Resource.success(appDatabase.shipDao().getAllShips())

    private val _shipDetail = MutableStateFlow<ShipsModel?>(null)
    val shipDetail: StateFlow<ShipsModel?> get() = _shipDetail

    suspend fun queryShipById(id: String) {
        _shipDetail.emit(appDatabase.shipDao().getShipById(id))
    }



}