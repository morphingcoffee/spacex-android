package com.morphingcoffee.spacex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompanyEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
}