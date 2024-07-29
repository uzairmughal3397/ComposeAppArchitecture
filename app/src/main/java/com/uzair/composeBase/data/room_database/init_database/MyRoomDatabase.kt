package com.uzair.composeBase.data.room_database.init_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uzair.composeBase.data.room_database.ships.ShipsDao
import com.uzair.composeBase.data.room_database.ships.ShipsModel
import javax.inject.Inject

@Database(
    entities = [ShipsModel::class],
    version = 1,
    exportSchema = false
)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun shipDao(): ShipsDao

    @Inject
    internal lateinit var myRoomDatabase: MyRoomDatabase

    companion object {
        internal const val DB_NAME = "DB_NAME"
    }

    fun deleteAll() {
        myRoomDatabase.clearAllTables()
    }
}