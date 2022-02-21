package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus
import com.morphingcoffee.spacex.domain.model.SortingOption
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
class GetLaunchesUseCaseTest {

    @MockK
    private lateinit var repository: ILaunchesRepository

    private lateinit var sortingCriteria: SortingOption
    private lateinit var launchStatusCriteria: FilteringOption.ByLaunchStatus
    private lateinit var yearCriteria: FilteringOption.ByYear

    // Subject
    private lateinit var launchesUseCase: GetLaunchesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        // Fake criteria
        sortingCriteria = SortingOption.Ascending
        launchStatusCriteria = FilteringOption.ByLaunchStatus(LaunchStatus.FutureLaunch)
        yearCriteria = FilteringOption.ByYear(1234)

        // Subject
        launchesUseCase = GetLaunchesUseCase(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `execute should return success result when repository provides empty collection`() {
        val noLaunches = listOf<Launch>()
        coEvery {
            repository.getAllLaunchesWithCriteria(any(), any(), any())
        } returns noLaunches

        runTest {
            val subject = launchesUseCase.execute()

            expectThat(subject)
                .isA<IGetLaunchesUseCase.Result.Success>()
                .get(IGetLaunchesUseCase.Result.Success::launches).isEqualTo(noLaunches)
            coVerify(exactly = 1) {
                repository.getAllLaunchesWithCriteria(any(), any(), any())
            }
        }
    }

    @Test
    fun `execute with custom criteria should call repository with descending sort and no filtering`() {
        val launches: List<Launch> = mockk()
        coEvery {
            repository.getAllLaunchesWithCriteria(any(), any(), any())
        } returns launches

        runTest {
            val subject =
                launchesUseCase.execute(sortingCriteria, launchStatusCriteria, yearCriteria)

            expectThat(subject)
                .isA<IGetLaunchesUseCase.Result.Success>()
                .get(IGetLaunchesUseCase.Result.Success::launches).isEqualTo(launches)
            coVerify(exactly = 1) {
                repository.getAllLaunchesWithCriteria(
                    sortingCriteria,
                    launchStatusCriteria,
                    yearCriteria
                )
            }
        }
    }

    @Test
    fun `execute with default criteria should call repository with descending sort and no filtering`() {
        val launches: List<Launch> = mockk()
        coEvery {
            repository.getAllLaunchesWithCriteria(any(), any(), any())
        } returns launches

        runTest {
            val subject = launchesUseCase.execute()

            expectThat(subject)
                .isA<IGetLaunchesUseCase.Result.Success>()
                .get(IGetLaunchesUseCase.Result.Success::launches).isEqualTo(launches)
            coVerify(exactly = 1) {
                repository.getAllLaunchesWithCriteria(SortingOption.Descending, null, null)
            }
        }
    }

    @Test
    fun `execute should swallow exception and return error result when repository throws`() {
        val errMsg = "fake error"
        coEvery {
            repository.getAllLaunchesWithCriteria(any(), any(), any())
        } throws Exception(errMsg)

        runTest {
            val subject =
                launchesUseCase.execute()

            expectThat(subject)
                .isA<IGetLaunchesUseCase.Result.Error>()
                .get(IGetLaunchesUseCase.Result.Error::t).isA<Exception>()
                .get(Throwable::message).isEqualTo(errMsg)
            coVerify(exactly = 1) {
                repository.getAllLaunchesWithCriteria(any(), any(), any())
            }
        }
    }

}