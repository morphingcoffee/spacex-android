package com.morphingcoffee.spacex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LaunchesDao {
    @Query("SELECT * FROM launches")
    fun getAll(): List<LaunchEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(launches: List<LaunchEntity>)
}