package com.morphingcoffee.spacex.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morphingcoffee.spacex.domain.model.Company

/**
 * [CompanyEntity] represents a row in the database.
 * Since at this point we expect to store only one entry, [uid] primary key is set to a constant (0).
 */
@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey(autoGenerate = false) val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "founder") val founder: String?,
    @ColumnInfo(name = "year_founded") val founded: Int?,
    @ColumnInfo(name = "employees") val employees: Int?,
    @ColumnInfo(name = "launch_sites") val launchSites: Int?,
    @ColumnInfo(name = "valuation_in_usd") val valuation: Long?,
)

fun CompanyEntity.toDomainModel(): Company =
    Company(
        companyName = name,
        founderName = founder,
        foundedYear = founded,
        numOfEmployees = employees,
        launchSites = launchSites,
        valuationInUsd = valuation,
    )