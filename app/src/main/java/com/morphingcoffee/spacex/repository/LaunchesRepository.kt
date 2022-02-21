package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.data.remote.model.*
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus
import com.morphingcoffee.spacex.domain.model.SortingOption
import com.morphingcoffee.spacex.repository.caching.ICurrentUnixTimeProvider
import com.morphingcoffee.spacex.repository.caching.LaunchesCachingConfig
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

class LaunchesRepository(
    private val launchesService: IFetchLaunchesService,
    private val db: AppDB,
    private val cachingConfig: LaunchesCachingConfig,
    private val currentUnixTimeProvider: ICurrentUnixTimeProvider
) : ILaunchesRepository {

    private var lastSuccessfulRemoteFetchTime: Long = 0L

    override suspend fun getAllLaunchesWithCriteria(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?
    ): List<Launch> {
        if (isCacheStillValid()) {
            return getFromDb(sortingOption, filterStatusOption, filterYearOption)
        }
        return try {
            fetchFromRemote(sortingOption, filterStatusOption, filterYearOption).also {
                lastSuccessfulRemoteFetchTime = currentUnixTimeProvider.currentTimeMillis()
            }
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            getFromDb(sortingOption, filterStatusOption, filterYearOption)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /** Retrieve all documents from remote to cache them, but apply filtering criteria before returning **/
    private suspend fun fetchFromRemote(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?
    ): List<Launch> {
        val sortDirection = if (sortingOption is SortingOption.Ascending) 1 else -1
        val paginationResponse = launchesService.fetchLaunchesWithRockets(
            LaunchesWithRocketsRequestBody(
                options = RequestOptions(
                    sort = SortByDateUnixOption(
                        sortDirection
                    )
                )
            )
        )
        val launchDtos = paginationResponse.body()?.docs

        // Update DB cache
        replaceAllDbEntries(launchDtos)

        // Filter on preferred criteria
        val filteredLaunches = launchDtos
            ?.filter {
                when {
                    filterYearOption == null -> true
                    it.dateUnix == null -> false
                    else -> {
                        val launchDateUtc =
                            LocalDateTime.ofEpochSecond(it.dateUnix, 0, ZoneOffset.UTC)
                        launchDateUtc.year == filterYearOption.year
                    }
                }
            }
            ?.filter {
                when (filterStatusOption?.status) {
                    LaunchStatus.Successful -> it.success == true
                    LaunchStatus.Failed -> it.success == false
                    LaunchStatus.FutureLaunch -> it.success == null
                    null -> true
                }
            }
            ?.map { it.toDomainModel() }
            ?.toList()
        return filteredLaunches ?: emptyList()
    }

    private fun getFromDb(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?,
    ): List<Launch> {
        val dao = db.launchesDao()
        val sortAscending = sortingOption == SortingOption.Ascending

        val launchYearCriteria: Int? = filterYearOption?.year
        val launchStatusCriteria: Boolean? = when (filterStatusOption?.status) {
            LaunchStatus.Successful -> true
            LaunchStatus.Failed -> false
            else -> null
        }

        return dao.getAllWithMatchingCriteria(
            sortAscending,
            launchStatusCriteria = launchStatusCriteria,
            launchYearCriteria = launchYearCriteria
        )
            ?.map { it.toDomainModel() }
            ?.toList()
            ?: emptyList()
    }

    private fun replaceAllDbEntries(launchDtos: List<LaunchWithRocketDto>?) {
        if (!launchDtos.isNullOrEmpty()) {
            db.launchesDao().deleteExistingAndInsertAll(launches = launchDtos.map { it.toEntity() })
        }
    }

    /**
     * Here we simply rely on [cachingConfig] configuration to reduce the remote lookup traffic
     * since we already have all the data in DB.
     **/
    private fun isCacheStillValid(): Boolean {
        val now = currentUnixTimeProvider.currentTimeMillis()
        val elapsed = abs(now - lastSuccessfulRemoteFetchTime)
        return elapsed <= cachingConfig.launchesCacheValidityInMillis
    }

}