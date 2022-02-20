package com.morphingcoffee.spacex.data.local

import androidx.room.ColumnInfo
import com.morphingcoffee.spacex.domain.model.Rocket

/** Used for embedding instead of explicitly storing it in its own separate table **/
data class RocketEntity(
    @ColumnInfo(name = "rocketId") val id: String,
    @ColumnInfo(name = "rocketName") val name: String?,
    @ColumnInfo(name = "rocketType") val type: String?,
)

fun RocketEntity.toDomainModel() = Rocket(name = name, type = type)