package com.uzair.composeBase.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uzair.composeBase.data.local.models.ShipsModel
import javax.inject.Inject

@Database(
    entities = [ShipsModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shipDao(): AppDao

    @Inject
    internal lateinit var myRoomDatabase: AppDatabase

    companion object {
        internal const val DB_NAME = "DB_NAME"
    }
}