package com.uzair.composeBase.data.repositories

import com.google.gson.Gson
import com.uzair.composeBase.R
import com.uzair.composeBase.compose.application.MainApplication
import com.uzair.composeBase.data.room_database.ResponseError
import com.uzair.composeBase.data.room_database.init_database.MyRoomDatabase
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import com.uzair.composeBase.di.NetworkHelper
import com.uzair.composeBase.domain.remote.ship_service.ShipService
import com.uzair.composeBase.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private var shipService: ShipService,
    private var myRoomDatabase: MyRoomDatabase,
    private val networkHelper: NetworkHelper
) {

    private val _shipsList = MutableStateFlow<Resource<MutableList<ShipsModel>>>(Resource.loading())
    val shipsList: StateFlow<Resource<MutableList<ShipsModel>>> get() = _shipsList

    suspend fun fetchShipsData() {
        val apiResponse = genericResponseHandler(networkHelper.isNetworkConnected()) {
            shipService.getShips()
        }
        apiResponse.value?.let {
            _shipsList.emit(it)

            when (it) {
                is Resource.Success -> {
                    saveShipDataIntoDatabase(it.data)
                }

                else -> {}
            }
        }


    }

    private suspend fun saveShipDataIntoDatabase(shipsModelList: MutableList<ShipsModel>) {
        ShipsModel.insertTheShips(shipsModelList, myRoomDatabase).collect()
    }

    suspend fun queryToGetAllShips() {
        _shipsList.emit(Resource.success(data = myRoomDatabase.shipDao().getAllShips()))
    }

    private val _shipDetail = MutableStateFlow<ShipsModel?>(null)
    val shipDetail: StateFlow<ShipsModel?> get() = _shipDetail

    suspend fun queryShipById(id: String) {
        _shipDetail.emit( myRoomDatabase.shipDao().getShipById(id))
    }





    private suspend fun <T> genericResponseHandler(
        isInternetConnected: Boolean,
        call: suspend () -> Response<T>?
    ): StateFlow<Resource<T>?> {
        val stateFlow = MutableStateFlow<Resource<T>?>(Resource.loading())

        if (isInternetConnected) {
            withContext(Dispatchers.IO) {
                try {
                    val response = call()
                    response?.let {
                        if (response.isSuccessful) {
                            stateFlow.emit(Resource.success(response.body()!!))
                        } else if (it.code() == 401 || it.code() == 400 || it.code() == 402 || it.code() == 403) {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            stateFlow.emit(
                                Resource.error(
                                    "", null, extractErrorMessage(
                                        errorMessagesJson
                                    )
                                )
                            )
                        } else {
                            stateFlow.emit(
                                Resource.error(
                                    MainApplication.applicationContext.getString(R.string.something_went_wrong),
                                    null, null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    stateFlow.emit(
                        Resource.error(
                            e.message ?: "Unknown Error", null, null
                        )
                    )
                }
            }
        } else {
            Resource.error(
                MainApplication.applicationContext.getString(
                    R.string.no_internet_connection
                ), null, null
            )

        }
        return stateFlow
    }

    private fun extractErrorMessage(errorMessagesJson: String): ResponseError {
        val gsonObj = Gson()

        gsonObj.serializeNulls()
        val errorObj = gsonObj.fromJson(errorMessagesJson, ResponseError::class.java)
        val msg = errorObj.message

        return errorObj
    }

}