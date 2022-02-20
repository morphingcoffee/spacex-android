package com.morphingcoffee.spacex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.morphingcoffee.spacex.data.local.model.CompanyEntity

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company LIMIT 1")
    fun get(): CompanyEntity?

    /**
     * Inserts & replaces existing company entry.
     * There is no need for explicit deletion as long as [CompanyEntity.uid] is a constant 0, as
     * it will always overwrite that single entry.
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(company: CompanyEntity)
}