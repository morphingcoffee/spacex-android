package com.morphingcoffee.spacex.data.local

import androidx.room.*
import com.morphingcoffee.spacex.data.local.model.LaunchEntity

@Dao
interface LaunchesDao {
    /**
     * Purge DB and insert provided [launches].
     *
     * This Delete + InsertAll transaction is created to be used for fully replacing the DB
     * knowledge base after a full data fetch.
     *
     * If we keep inserting items with replacement, items which are no longer served by the remote
     * will get stale in DB and will still be displayed to the user. [deleteExistingAndInsertAll]
     * avoids accumulation of such stale data.
     **/
    @Transaction
    fun deleteExistingAndInsertAll(launches: List<LaunchEntity>) {
        deleteAll()
        insertAll(launches)
    }

    /**
     * Get all entries sorted by preferred [sortAscending] flag.
     * Optional [launchStatusCriteria] can be passed to apply filtering based on launch status:
     *  - when null is passed, there is no filtering applied
     *  - when true is passed, only successful launches are returned
     *  - when false is passed, only failed launches are returned
     * Optional [launchYearCriteria] can be passed to apply filtering based on launch year:
     *  - when null is passed, there is no filtering applied
     *  - when a number is passed, only launches executed/planned for that year are returned
     **/
    @Query(
        """SELECT * FROM launches
                 WHERE
                    CASE
                        WHEN :launchStatusCriteria IS NULL THEN 1
                        ELSE success = :launchStatusCriteria
                    END
                 AND
                    CASE
                        WHEN :launchYearCriteria IS NULL THEN 1
                        ELSE year = :launchYearCriteria
                    END
                 ORDER BY
                   CASE WHEN :sortAscending = 1 THEN dateUnix END ASC,
                   CASE WHEN :sortAscending = 0 THEN dateUnix END DESC
              """
    )
    fun getAllWithMatchingCriteria(
        sortAscending: Boolean,
        launchStatusCriteria: Boolean?,
        launchYearCriteria: Int?
    ): List<LaunchEntity>?

    @Query("DELETE FROM launches")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(launches: List<LaunchEntity>)
}