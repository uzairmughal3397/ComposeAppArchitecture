package com.uzair.composeBase.data.repositories

import com.google.gson.Gson
import com.uzair.composeBase.R
import com.uzair.composeBase.compose.application.MainApplication
import com.uzair.composeBase.data.genericModelClasses.ResponseError
import com.uzair.composeBase.data.room_database.init_database.MyRoomDatabase
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import com.uzair.composeBase.di.NetworkHelper
import com.uzair.composeBase.domain.remote.ship_service.ApiService
import com.uzair.composeBase.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private var apiService: ApiService,
    private var myRoomDatabase: MyRoomDatabase,
    private val networkHelper: NetworkHelper
) {

    suspend fun fetchShipsData() =
        handleApiResponse(networkHelper.isNetworkConnected()) {
            apiService.getShips()
        }

    suspend fun saveShipDataIntoDatabase(shipsModelList: MutableList<ShipsModel>) {
        ShipsModel.insertTheShips(shipsModelList, myRoomDatabase).collect()
    }

    suspend fun queryToGetAllShips() =
//        Resource.success(ResponseTemplate(null,null,myRoomDatabase.shipDao().getAllShips(),null))
        Resource.success(myRoomDatabase.shipDao().getAllShips())

    private val _shipDetail = MutableStateFlow<ShipsModel?>(null)
    val shipDetail: StateFlow<ShipsModel?> get() = _shipDetail

    suspend fun queryShipById(id: String) {
        _shipDetail.emit(myRoomDatabase.shipDao().getShipById(id))
    }


    private suspend fun <T> handleApiResponse(
        isInternetConnected: Boolean,
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return try {
            if (isInternetConnected) {
                val c = apiCall()
                c.let {
                    if (c.isSuccessful) {

                        return Resource.success(c.body()!!)

                    } else if (it.code() == 401 || it.code() == 400 || it.code() == 402 || it.code() == 403) {
                        val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!

                        return Resource.error(
                            "", null, extractErrorMessage(
                                errorMessagesJson
                            )
                        )

                    } else {
                        return Resource.error(
                            MainApplication.applicationContext.getString(R.string.something_went_wrong),
                            null, null
                        )

                    }
                }
            }else{
                Resource.error(
                    MainApplication.applicationContext.getString(
                        R.string.no_internet_connection
                    ), null, null
                )
            }
        } catch (e: Exception) {
            Resource.error(e.message!!, null, null)
        }
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