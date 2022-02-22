package com.morphingcoffee.spacex.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.FakeCompanyEntity1
import shared.fakes.FakeCompanyEntity2
import java.io.IOException

class CompanyDaoDBTest {

    private lateinit var companyDao: CompanyDao
    private lateinit var db: AppDB

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java).build()
        companyDao = db.companyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertingCompanyToDbShouldReturnSameCompany() {
        // Subject
        companyDao.insertOrUpdate(company = FakeCompanyEntity1)
        val actualCompany = companyDao.get()

        // Assert successful insertion
        assertEquals(FakeCompanyEntity1, actualCompany)
    }

    @Test
    @Throws(Exception::class)
    fun updatingCompanyInDbShouldReturnUpdatedCompany() {
        // Subject
        companyDao.insertOrUpdate(company = FakeCompanyEntity1)
        companyDao.insertOrUpdate(company = FakeCompanyEntity2)
        val actualCompany = companyDao.get()

        // Assert successful update
        assertEquals(FakeCompanyEntity2, actualCompany)
    }
}