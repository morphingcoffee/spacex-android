package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.data.remote.model.LaunchWithRocketDto
import com.morphingcoffee.spacex.data.remote.model.LaunchesWithRocketsRequestBody
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
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
    override suspend fun getLaunches(
        sortingOption: SortingOption,
        filterOptions: List<FilteringOption>
    ): List<Launch> {
        return try {
            fetchAllFromRemote(sortingOption, filterOptions)
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            getFromDb(sortingOption, filterOptions)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // TODO remove [filterOptions] from [fetchAllFromRemote]. This will do a full fetch, and we'll filter from DB
    private suspend fun fetchAllFromRemote(
        sortingOption: SortingOption,
        filterOptions: Iterable<FilteringOption>
    ): List<Launch> {
        val paginationResponse = launchesService.fetchLaunchesWithRockets(
            LaunchesWithRocketsRequestBody()
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
        filterOptions: List<FilteringOption>
    ): List<Launch> {
        val dao = db.launchesDao()
        val sortAscending = sortingOption == SortingOption.Ascending
        var filterYearCriteria: Int? = null
        var launchStatusCriteria: Boolean? = null
        // Unpack filter options & translate to primitive values
        filterOptions.forEach { option ->
            when (option) {
                is FilteringOption.ByLaunchStatus -> launchStatusCriteria =
                    option.status == LaunchStatus.Successful
                is FilteringOption.ByYear -> filterYearCriteria = option.year
            }
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