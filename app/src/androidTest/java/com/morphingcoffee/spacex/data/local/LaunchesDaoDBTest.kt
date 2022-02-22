package com.morphingcoffee.spacex.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.FakeFailedLaunchEntity2015
import shared.fakes.FakeFailedLaunchEntity2016
import shared.fakes.FakeSuccessfulLaunchEntity2015
import shared.fakes.FakeSuccessfulLaunchEntity2016
import java.io.IOException

class LaunchesDaoDBTest {

    private lateinit var launchesDao: LaunchesDao
    private lateinit var db: AppDB

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java).build()
        launchesDao = db.launchesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun dbShouldReturn_orderedLaunches_whenOptionAscending() {
        val launches = listOf(
            FakeSuccessfulLaunchEntity2016,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2015,
            FakeFailedLaunchEntity2016
        )

        // Subject
        launchesDao.deleteExistingAndInsertAll(launches)
        val actualLaunches = launchesDao.getAllWithMatchingCriteria(
            sortAscending = true,
            launchYearCriteria = null,
            launchStatusCriteria = null
        )

        // Assert successful insertion & sorting order on retrieval
        assertEquals(launches.size, actualLaunches!!.size)
        val expected = listOf(
            FakeFailedLaunchEntity2015,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2016,
            FakeSuccessfulLaunchEntity2016
        )
        assertEquals(expected, actualLaunches)
    }

    @Test
    fun dbShouldReturn_orderedLaunches_whenOptionDescending() {
        val launches = listOf(
            FakeSuccessfulLaunchEntity2016,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2015,
            FakeFailedLaunchEntity2016
        )

        // Subject
        launchesDao.deleteExistingAndInsertAll(launches)
        val actualLaunches = launchesDao.getAllWithMatchingCriteria(
            sortAscending = false,
            launchYearCriteria = null,
            launchStatusCriteria = null
        )

        // Assert successful insertion & sorting order on retrieval
        assertEquals(launches.size, actualLaunches!!.size)
        val expected = listOf(
            FakeSuccessfulLaunchEntity2016,
            FakeFailedLaunchEntity2016,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2015
        )
        assertEquals(expected, actualLaunches)
    }

    @Test
    fun dbShouldReturn_filteredLaunches_whenOptionByYear() {
        val launches = listOf(
            FakeSuccessfulLaunchEntity2016,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2015,
            FakeFailedLaunchEntity2016
        )

        // Subject
        launchesDao.deleteExistingAndInsertAll(launches)
        val actualLaunches = launchesDao.getAllWithMatchingCriteria(
            sortAscending = true,
            // 2016 year filter
            launchYearCriteria = 2016,
            launchStatusCriteria = null
        )

        // Assert successful insertion & sorting order on retrieval
        assertEquals(2, actualLaunches!!.size)
        val expected = listOf(
            FakeFailedLaunchEntity2016,
            FakeSuccessfulLaunchEntity2016,
        )
        assertEquals(expected, actualLaunches)
    }

    @Test
    fun dbShouldReturn_filteredLaunches_whenOptionByStatus() {
        val launches = listOf(
            FakeSuccessfulLaunchEntity2016,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2015,
            FakeFailedLaunchEntity2016
        )

        // Subject
        launchesDao.deleteExistingAndInsertAll(launches)
        val actualLaunches = launchesDao.getAllWithMatchingCriteria(
            sortAscending = true,
            launchYearCriteria = null,
            // failed launch status
            launchStatusCriteria = false
        )

        // Assert successful insertion & sorting order on retrieval
        assertEquals(2, actualLaunches!!.size)
        val expected = listOf(
            FakeFailedLaunchEntity2015,
            FakeFailedLaunchEntity2016
        )
        assertEquals(expected, actualLaunches)
    }

    @Test
    fun dbShouldReturn_filteredLaunches_whenOptionByYearAndStatus() {
        val launches = listOf(
            FakeSuccessfulLaunchEntity2016,
            FakeSuccessfulLaunchEntity2015,
            FakeFailedLaunchEntity2015,
            FakeFailedLaunchEntity2016
        )

        // Subject
        launchesDao.deleteExistingAndInsertAll(launches)
        val actualLaunches = launchesDao.getAllWithMatchingCriteria(
            sortAscending = true,
            // 2016 year filter
            launchYearCriteria = 2016,
            // failed launch status
            launchStatusCriteria = false
        )

        // Assert successful insertion & sorting order on retrieval
        assertEquals(1, actualLaunches!!.size)
        val expected = listOf(
            FakeFailedLaunchEntity2016
        )
        assertEquals(expected, actualLaunches)
    }
}