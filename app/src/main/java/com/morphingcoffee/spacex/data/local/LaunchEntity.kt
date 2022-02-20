package com.morphingcoffee.spacex.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus

/**
 * [LaunchEntity] represents a row in the database.
 */
@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey(autoGenerate = false) val uid: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "success") val success: Boolean?,
    @ColumnInfo(name = "dateUtc") val dateUtc: String?,
    @ColumnInfo(name = "dateUnix") val dateUnix: Long?,
    @Embedded val links: LaunchLinksEntity?,
    @Embedded val rocket: RocketEntity?,
)

fun LaunchEntity.toDomainModel(): Launch {
    val status = when (success) {
        true -> LaunchStatus.Successful
        false -> LaunchStatus.Failed
        else -> LaunchStatus.FutureLaunch
    }
    return Launch(
        id = uid,
        name = name,
        launchDateTime = dateUtc,
        launchStatus = status,
        rocket = rocket?.toDomainModel(),
        links = links?.toDomainModel(),
    )
}