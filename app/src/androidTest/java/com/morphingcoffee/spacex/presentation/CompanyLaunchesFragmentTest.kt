package com.morphingcoffee.spacex.presentation

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import coil.request.ImageRequest
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.presentation.recyclerview.LaunchesAdapter
import com.morphingcoffee.spacex.presentation.recyclerview.LaunchesDiffUtilCallback
import com.morphingcoffee.spacex.presentation.time.ICurrentUnixTimeProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module


/**
 * Espresso UI test for company & launches fragment.
 * ViewModels are mocked and arbitrary LiveData values are broadcast to simulate different scenarios.
 **/
@ExperimentalCoroutinesApi
class CompanyLaunchesFragmentTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var imageLoader: ImageLoader

    @MockK
    private lateinit var imageRequestBuilder: ImageRequest.Builder

    @MockK(relaxed = true)
    private lateinit var companyViewModel: CompanyViewModel

    @MockK(relaxed = true)
    private lateinit var launchesViewModel: LaunchesViewModel

    private lateinit var companyUiState: MutableLiveData<CompanyViewModel.UiState>
    private lateinit var launchesUiState: MutableLiveData<LaunchesViewModel.UiState>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        // Set up fake LiveDatas which can be used to trigger broadcasts
        companyUiState = MutableLiveData()
        every { companyViewModel.state } returns companyUiState
        launchesUiState = MutableLiveData()
        every { launchesViewModel.uiState } returns launchesUiState

        // Provide dependencies
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(
                // Mocks
                module {
                    factory<ImageLoader> { imageLoader }
                    factory<ImageRequest.Builder> { imageRequestBuilder }
                    factory<LaunchesAdapter> {
                        LaunchesAdapter(
                            get(),
                            get(),
                            get(),
                            get(),
                            get()
                        )
                    }
                    viewModel { companyViewModel }
                    viewModel { launchesViewModel }
                },
                // Fakes
                module {
                    factory<ICurrentUnixTimeProvider> { ICurrentUnixTimeProvider { 0L } }
                },
                // Real impl
                module {
                    factory<DiffUtil.ItemCallback<Launch>> { LaunchesDiffUtilCallback() }
                    factory<AsyncDifferConfig<Launch>> {
                        AsyncDifferConfig.Builder<Launch>(get()).build()
                    }
                },
            )
        }
    }

    @After
    fun tearDown() {
        // Tear down DI container
        stopKoin()
    }

    // region Company section

    @Test
    fun shouldDisplay_companyHeading() {
        // Launch fragment
        launchFragmentInContainer<CompanyLaunchesFragment>()

        // Assert
        onView(withText("COMPANY")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShow_companyLoadingState_whenLoadingCompany() {
        // Imitate loading state
        companyUiState.postValue(CompanyViewModel.UiState.Loading)

        // Launch fragment
        launchFragmentInContainer<CompanyLaunchesFragment>()

        // Assert
        onView(withText("Loading…")).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldShow_companyDisplayState_whenCompanyLoaded() {
        // Imitate loading state
        val fakeCompany = Company(
            companyName = "name",
            foundedYear = 2000,
            founderName = "founder",
            numOfEmployees = 12345,
            launchSites = 3,
            valuationInUsd = 100000,
        )
        companyUiState.postValue(CompanyViewModel.UiState.Display(fakeCompany))

        // Launch fragment
        launchFragmentInContainer<CompanyLaunchesFragment>()

        // Assert
        with(fakeCompany) {
            onView(withText("$companyName was founded by $founderName in ${foundedYear}. It has now $numOfEmployees employees, $launchSites launch sites, and is valued at USD $valuationInUsd")).check(
                matches(isDisplayed())
            )
        }
    }

    @Ignore("Display Toast assertion ignored. Works fine on an emulator, but seems to hang indefinitely on a real device")
    @Test
    fun shouldShow_companyErrorState_whenCompanyLoadingFails() {
        // Imitate error state
        val fakeErrorRes = R.string.unknown_error_message
        companyUiState.postValue(CompanyViewModel.UiState.Error(fakeErrorRes))

        // Launch fragment
        val scenario = launchFragmentInContainer<CompanyLaunchesFragment>()
        var decorView: View? = null
        scenario.onFragment {
            decorView = it.requireActivity().window.decorView
        }

        // Assert correct display of a Toast
        assertNotSame(null, decorView)
        onView(withText("Could not get the latest data. Check connection & try again")).inRoot(
            withDecorView(not(`is`(decorView)))
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    // endregion

    // region Launches section

    @Test
    fun shouldDisplay_launchesHeading() = runTest {
        // Launch fragment
        launchFragmentInContainer<CompanyLaunchesFragment>()

        // Assert
        onView(withText("LAUNCHES")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShow_launchesLoadingState_whenLoadingLaunches() {
        // Imitate loading state
        launchesUiState.postValue(LaunchesViewModel.UiState.Loading)

        // Launch fragment
        launchFragmentInContainer<CompanyLaunchesFragment>()

        // Assert swipe refresh isRefreshing state
        onView(withId(R.id.swipeRefresh)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldShow_launchesErrorState_whenLoadingLaunchesFails() {
        // Imitate loading state
        val fakeErrorRes = R.string.unknown_error_message
        launchesUiState.postValue(LaunchesViewModel.UiState.Error(fakeErrorRes))

        // Launch fragment
        launchFragmentInContainer<CompanyLaunchesFragment>()

        // Assert
        onView(withText("No launches with matching criteria\nSwipe down to refresh")).check(
            matches(isDisplayed())
        )
    }

    // endregion
}