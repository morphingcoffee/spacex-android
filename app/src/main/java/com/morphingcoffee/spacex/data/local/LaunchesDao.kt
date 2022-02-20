package com.morphingcoffee.spacex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LaunchesDao {
    @Query(
        "SELECT * FROM launches ORDER BY CASE WHEN :sortAscending = 1 THEN dateUnix END ASC," +
                "CASE WHEN :sortAscending = 0 THEN dateUnix END DESC"
    )
    fun getAll(sortAscending: Boolean): List<LaunchEntity>?

//    @Query("SELECT * FROM launches WHERE success = :status")
//    fun getAllWithLaunchStatus(status: Boolean): List<LaunchEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(launches: List<LaunchEntity>)
}