package com.uzair.composeBase.data.local.models

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity
data class ShipsModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var ship_id: String,
    var ship_name: String?,
    var ship_type: String?,
    var active: Boolean?,
    var imo: Long?,
    var mmsi: Long?,
    var abs: Long?,
    @SerializedName("class")
    var clazz: Long?,
    var weight_lbs: Long?,
    var year_built: Long?,
    var home_port: String?,
    var status: String?,
    var speed_kn: Int?,
    var course_deg: String?,
    var positionId: Long,
    var successful_landings: Int?,
    var attempted_landings: Int?,
    var url: String?,
    var image: String?,
)