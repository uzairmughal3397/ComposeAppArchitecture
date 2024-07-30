package com.uzair.composeBase.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uzair.composeBase.data.local.models.ShipsModel

@Dao
interface AppDao {

    @Query("SELECT * FROM shipsmodel")
    suspend fun getAllShips(): MutableList<ShipsModel>

    @Query("SELECT * FROM shipsmodel WHERE ship_id=:id")
    suspend fun getShipById(id: String): ShipsModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipModel(shipsModel: ShipsModel)
}