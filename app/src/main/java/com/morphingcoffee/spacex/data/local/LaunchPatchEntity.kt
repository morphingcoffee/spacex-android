package com.morphingcoffee.spacex.data.local

import androidx.room.ColumnInfo
import com.morphingcoffee.spacex.domain.model.PatchImage

/** Used for embedding instead of explicitly storing it in its own separate table **/
data class LaunchPatchEntity(
    @ColumnInfo(name = "smallPatch") val small: String?,
    @ColumnInfo(name = "largePatch") val large: String?
)

fun LaunchPatchEntity.toDomainModel() = PatchImage(
    smallURL = small,
    largeURL = large,
)
