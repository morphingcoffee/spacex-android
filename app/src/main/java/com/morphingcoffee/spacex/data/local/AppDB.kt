package com.morphingcoffee.spacex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.morphingcoffee.spacex.data.local.model.CompanyEntity
import com.morphingcoffee.spacex.data.local.model.LaunchEntity

@Database(entities = [CompanyEntity::class, LaunchEntity::class], version = 4)
abstract class AppDB : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun launchesDao(): LaunchesDao
}