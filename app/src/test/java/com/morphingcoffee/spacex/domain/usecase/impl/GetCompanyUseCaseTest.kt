package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class GetCompanyUseCaseTest {

    @MockK
    private lateinit var repository: ICompanyRepository

    // Subject
    private lateinit var companyUseCase: GetCompanyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        // Subject
        companyUseCase = GetCompanyUseCase(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `execute should return success result with company data when repository provides non-null data`() {
        val company: Company = mockk()
        coEvery { repository.getCompany() } returns company

        runTest {
            val subject = companyUseCase.execute()

            expectThat(subject)
                .isA<IGetCompanyUseCase.Result.Success>()
                .isEqualTo(IGetCompanyUseCase.Result.Success(company = company))
        }
    }

    @Test
    fun `execute should return error result when repository provides null data`() {
        coEvery { repository.getCompany() } returns null

        runTest {
            val subject = companyUseCase.execute()

            expectThat(subject)
                .isA<IGetCompanyUseCase.Result.Error>()
                .get(IGetCompanyUseCase.Result.Error::t).isA<RuntimeException>()
                .get(Throwable::message).isEqualTo("No data")
        }
    }

    @Test
    fun `execute should swallow exception and return error result when repository throws`() {
        val errMsg = "fake error"
        coEvery { repository.getCompany() } throws Exception(errMsg)

        runTest {
            val subject = companyUseCase.execute()

            expectThat(subject)
                .isA<IGetCompanyUseCase.Result.Error>()
                .get(IGetCompanyUseCase.Result.Error::t).isA<Exception>()
                .get(Throwable::message).isEqualTo(errMsg)
        }
    }

}