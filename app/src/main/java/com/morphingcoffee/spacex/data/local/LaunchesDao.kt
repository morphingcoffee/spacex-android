package com.morphingcoffee.spacex.data.local

import androidx.room.*
import com.morphingcoffee.spacex.data.local.model.LaunchEntity

@Dao
interface LaunchesDao {
    /**
     * Get all entries sorted by preferred [sortAscending] flag.
     * Optional [launchStatusCriteria] can be passed to apply filtering based on launch status:
     *  - when null is passed, there is no filtering applied
     *  - when true is passed, only successful launches are returned
     *  - when false is passed, only failed launches are returned
     **/
    @Query(
        """SELECT * FROM launches
                 WHERE
                    CASE
                        WHEN :launchStatusCriteria = 1 THEN success = 1
                        WHEN :launchStatusCriteria = 0 THEN success = 0
                        ELSE 1=1
                    END
                 ORDER BY
                   CASE WHEN :sortAscending = 1 THEN dateUnix END ASC,
                   CASE WHEN :sortAscending = 0 THEN dateUnix END DESC
              """
    )
    fun getAllWithMatchingCriteria(
        sortAscending: Boolean,
        launchStatusCriteria: Boolean?
    ): List<LaunchEntity>?

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

    @Query("DELETE FROM launches")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(launches: List<LaunchEntity>)
}