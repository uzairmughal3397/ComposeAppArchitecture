package com.uzair.composeBase.compose.ships_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzair.composeBase.data.repositories.MainRepository
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import com.uzair.composeBase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipsViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val shipsModelStateFlow : StateFlow<Resource<MutableList<ShipsModel>>>
        get()= mainRepository.shipsList

    init {
        viewModelScope.launch {
            requestForShipsData()
            offline()
        }
    }

    private fun requestForShipsData() = viewModelScope.launch {
        mainRepository.fetchShipsData()
    }

    private fun offline() =  viewModelScope.launch {
        mainRepository.queryToGetAllShips()
    }

}