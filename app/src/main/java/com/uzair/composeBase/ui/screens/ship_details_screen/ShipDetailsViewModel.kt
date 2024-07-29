package com.uzair.composeBase.ui.screens.ship_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzair.composeBase.data.repositories.MainRepository
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipDetailsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val shipDetails : StateFlow<ShipsModel?>
    get()= mainRepository.shipDetail

    fun queryShipById(id: String) = viewModelScope.launch {
        mainRepository.queryShipById(id =id )
    }

}