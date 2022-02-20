package com.morphingcoffee.spacex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompanyEntity::class, LaunchEntity::class], version = 2)
abstract class AppDB : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun launchesDao(): LaunchesDao
}