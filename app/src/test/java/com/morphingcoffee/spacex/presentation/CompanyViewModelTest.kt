package com.morphingcoffee.spacex.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class CompanyViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getCompanyUseCase: IGetCompanyUseCase

    // Subject
    private lateinit var companyViewModel: CompanyViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        // Set up [companyViewModel] subject in individual cases after mocking since it auto-starts fetching.
        // Use Dispatchers.Main which points to test setup
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `view model should automatically get company on init`() =
        runTest {
            // Mock
            val company: Company = mockk()
            coEvery { getCompanyUseCase.execute() } returns IGetCompanyUseCase.Result.Success(
                company
            )

            // Subject
            companyViewModel = CompanyViewModel(
                getCompanyUseCase = getCompanyUseCase,
                defaultDispatcher = Dispatchers.Main
            )

            // Validate
            advanceUntilIdle()
            coVerify(exactly = 1) { getCompanyUseCase.execute() }
            expectThat(companyViewModel.state.value).isA<CompanyViewModel.UiState.Display>()
        }

    @Test
    fun `refresh user action should get company and switch to display ui state when use case returns success`() =
        runTest {
            // Mock error state initially
            coEvery { getCompanyUseCase.execute() } returns IGetCompanyUseCase.Result.Error(
                Exception("fake")
            )

            // Subject
            companyViewModel = CompanyViewModel(
                getCompanyUseCase = getCompanyUseCase,
                defaultDispatcher = Dispatchers.Main
            )

            // Change use case response to success
            val company: Company = mockk()
            coEvery { getCompanyUseCase.execute() } returns IGetCompanyUseCase.Result.Success(
                company
            )

            // Execute refresh action
            companyViewModel.handleUserAction(CompanyViewModel.UserAction.Refresh)

            // Validate
            advanceUntilIdle()
            with(expectThat(companyViewModel.state.value).isA<CompanyViewModel.UiState.Display>()) {
                get(CompanyViewModel.UiState.Display::company).isEqualTo(company)
            }
        }

    @Test
    fun `refresh user action should get company and switch to error ui state when use case returns error`() =
        runTest {
            // Mock success state initially
            val company: Company = mockk()
            coEvery { getCompanyUseCase.execute() } returns IGetCompanyUseCase.Result.Success(
                company
            )

            // Subject
            companyViewModel = CompanyViewModel(
                getCompanyUseCase = getCompanyUseCase,
                defaultDispatcher = Dispatchers.Main
            )

            // Change use case response to failure
            coEvery { getCompanyUseCase.execute() } returns IGetCompanyUseCase.Result.Error(
                Exception("fake")
            )

            // Execute refresh action
            companyViewModel.handleUserAction(CompanyViewModel.UserAction.Refresh)

            // Validate
            advanceUntilIdle()
            with(expectThat(companyViewModel.state.value).isA<CompanyViewModel.UiState.Error>()) {
                get(CompanyViewModel.UiState.Error::errorRes).isEqualTo(R.string.unknown_error_message)
            }
        }
}