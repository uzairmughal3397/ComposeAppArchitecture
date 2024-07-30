package com.uzair.composeBase.ui.screens.shipsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzair.composeBase.data.remote.repository.MainRepository
import com.uzair.composeBase.data.local.models.ShipsModel
import com.uzair.composeBase.data.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipsViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _shipsModelStateFlow: MutableStateFlow<Resource<MutableList<ShipsModel>>> =
        MutableStateFlow(Resource.loading())
    val shipsModelStateFlow = _shipsModelStateFlow.asStateFlow()
    init {
        viewModelScope.launch {
            requestForShipsData()
            offline()
        }
    }

    private fun requestForShipsData() = viewModelScope.launch {
        _shipsModelStateFlow.emit(mainRepository.fetchShipsData())
    }

    private fun offline() =  viewModelScope.launch {
        _shipsModelStateFlow.emit(mainRepository.queryToGetAllShips())
    }

    fun addToDatabase(data: MutableList<ShipsModel>) {
        viewModelScope.launch {
            mainRepository.saveShipDataIntoDatabase(data)
        }
    }

}