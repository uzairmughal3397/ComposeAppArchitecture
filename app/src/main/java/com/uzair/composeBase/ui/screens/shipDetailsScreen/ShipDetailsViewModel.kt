package com.uzair.composeBase.ui.screens.shipDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzair.composeBase.data.remote.repository.MainRepository
import com.uzair.composeBase.data.local.models.ShipsModel
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