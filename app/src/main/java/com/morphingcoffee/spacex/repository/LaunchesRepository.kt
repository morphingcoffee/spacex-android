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
import java.net.UnknownHostException

class LaunchesRepository(
    private val launchesService: IFetchLaunchesService,
    private val db: AppDB
) : ILaunchesRepository {
    override suspend fun getAllLaunches(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?
    ): List<Launch> {
        return try {
            fetchAllFromRemote(sortingOption)
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            getFromDb(sortingOption, filterStatusOption, filterYearOption)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // TODO remove [filterOptions] from [fetchAllFromRemote]. This will do a full fetch, and we'll filter from DB
    private suspend fun fetchAllFromRemote(
        sortingOption: SortingOption
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
        val paginationDto = paginationResponse.body()
        val launchDtos = paginationDto?.docs
        replaceDbEntries(launchDtos)
        val allLaunches = launchDtos
            ?.map { it.toDomainModel() }
            ?.toList()
        return allLaunches ?: emptyList()
    }

    private fun getFromDb(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?,
    ): List<Launch> {
        val dao = db.launchesDao()
        val sortAscending = sortingOption == SortingOption.Ascending

        val filterYearCriteria: Int? = filterYearOption?.year
        val launchStatusCriteria: Boolean? = when (filterStatusOption?.status) {
            LaunchStatus.Successful -> true
            LaunchStatus.Failed -> false
            else -> null
        }

        return dao.getAllWithMatchingCriteria(
            sortAscending,
            launchStatusCriteria = launchStatusCriteria
        )
            ?.map { it.toDomainModel() }
            ?.toList()
            ?: emptyList()
    }

    private fun replaceDbEntries(launchDtos: List<LaunchWithRocketDto>?) {
        if (!launchDtos.isNullOrEmpty()) {
            db.launchesDao().deleteExistingAndInsertAll(launches = launchDtos.map { it.toEntity() })
        }
    }

}