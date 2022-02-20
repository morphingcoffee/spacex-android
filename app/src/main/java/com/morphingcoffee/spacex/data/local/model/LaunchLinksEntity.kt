package com.morphingcoffee.spacex.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.morphingcoffee.spacex.domain.model.Links

/** Used for embedding instead of explicitly storing it in its own separate table **/
data class LaunchLinksEntity(
    @ColumnInfo(name = "youtubeURL") val youtubeURL: String?,
    @ColumnInfo(name = "articleURL") val articleURL: String?,
    @ColumnInfo(name = "wikiURL") val wikiURL: String?,
    @Embedded val patchEntity: LaunchPatchEntity?,
)

fun LaunchLinksEntity.toDomainModel() = Links(
    webcastURL = youtubeURL,
    articleURL = articleURL,
    wikiURL = wikiURL,
    patchImage = patchEntity?.toDomainModel()
)