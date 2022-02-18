package com.morphingcoffee.spacex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company LIMIT 1")
    fun get(): CompanyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(company: CompanyEntity)
}