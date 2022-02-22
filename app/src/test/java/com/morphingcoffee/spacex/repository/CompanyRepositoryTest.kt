package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.model.CompanyEntity
import com.morphingcoffee.spacex.data.remote.IFetchCompanyService
import com.morphingcoffee.spacex.data.remote.model.CompanyDto
import com.morphingcoffee.spacex.domain.model.Company
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class CompanyRepositoryTest {

    @MockK
    private lateinit var service: IFetchCompanyService

    @MockK
    private lateinit var db: AppDB

    // Subject
    private lateinit var companyRepository: CompanyRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        // Subject
        companyRepository = CompanyRepository(companyService = service, db = db)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getCompany should update db and return company when service returns data`() {
        val companyDto: CompanyDto = mockk(relaxed = true)
        coEvery { service.fetchCompany().body() } returns companyDto
        val companyEntitySlot = slot<CompanyEntity>()
        coEvery { db.companyDao().insertOrUpdate(capture(companyEntitySlot)) } just Runs

        runTest {
            val subject = companyRepository.getCompany()
            val capturedEntity = companyEntitySlot.captured

            expectThat(subject)
                .isA<Company>().apply {
                    get(Company::companyName).isEqualTo(capturedEntity.name)
                    get(Company::founderName).isEqualTo(capturedEntity.founder)
                    get(Company::foundedYear).isEqualTo(capturedEntity.founded)
                    get(Company::numOfEmployees).isEqualTo(capturedEntity.employees)
                    get(Company::launchSites).isEqualTo(capturedEntity.launchSites)
                    get(Company::valuationInUsd).isEqualTo(capturedEntity.valuation)
                }
            coVerify(exactly = 1) {
                service.fetchCompany()
                db.companyDao().insertOrUpdate(any())
            }
        }
    }

    @Test
    fun `getCompany should swallow UnknownHostException exception when service throws and get data from db`() {
        val errMsg = "fake error"
        val companyEntity: CompanyEntity = mockk(relaxed = true)
        coEvery { service.fetchCompany() } throws UnknownHostException(errMsg)
        coEvery { db.companyDao().get() } returns companyEntity

        runTest {
            val subject = companyRepository.getCompany()

            expectThat(subject)
                .isA<Company>().apply {
                    get(Company::companyName).isEqualTo(companyEntity.name)
                    get(Company::founderName).isEqualTo(companyEntity.founder)
                    get(Company::foundedYear).isEqualTo(companyEntity.founded)
                    get(Company::numOfEmployees).isEqualTo(companyEntity.employees)
                    get(Company::launchSites).isEqualTo(companyEntity.launchSites)
                    get(Company::valuationInUsd).isEqualTo(companyEntity.valuation)
                }
            coVerify(exactly = 1) {
                service.fetchCompany()
                db.companyDao().get()
            }
        }
    }

    @Test
    fun `getCompany should swallow general exception and return null when service throws`() {
        val errMsg = "fake error"
        coEvery { service.fetchCompany() } throws Exception(errMsg)

        runTest {
            val subject = companyRepository.getCompany()

            expectThat(subject)
                .isEqualTo(null)
            coVerify(exactly = 1) {
                service.fetchCompany()
                // ensure no DB lookup on general exception
                db wasNot Called
            }
        }
    }

}