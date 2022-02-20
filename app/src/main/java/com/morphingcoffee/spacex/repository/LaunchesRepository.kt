package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.toDomainModel
import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.data.remote.model.LaunchWithRocketDto
import com.morphingcoffee.spacex.data.remote.model.LaunchesWithRocketsRequestBody
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.Launch
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
            fetchFromRemote(sortingOption, filterOptions)
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            getFromDb(sortingOption, filterOptions)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun fetchFromRemote(
        sortingOption: SortingOption,
        filterOptions: Iterable<FilteringOption>
    ): List<Launch> {
        val paginationResponse = launchesService.fetchLaunchesWithRockets(
            LaunchesWithRocketsRequestBody()
        )
        val paginationDto = paginationResponse.body()
        val launchDtos = paginationDto?.docs
        updateDb(launchDtos)
        val launches = launchDtos
            ?.map { it.toDomainModel() }
            ?.toList()
        return launches ?: emptyList()
    }

    private fun getFromDb(
        sortingOption: SortingOption,
        filterOptions: List<FilteringOption>
    ): List<Launch> {
        val dao = db.launchesDao()
        val sortAscending = sortingOption == SortingOption.Ascending
        if (filterOptions.isNotEmpty()) {
//            return dao.getAllWithLaunchStatus()
//                ?.map { it.toDomainModel() }
//                ?.toList()
//                ?: emptyList()
            TODO("wip")
        } else {
            return dao.getAll(sortAscending)
                ?.map { it.toDomainModel() }
                ?.toList()
                ?: emptyList()
        }
    }

    private fun updateDb(launchDtos: List<LaunchWithRocketDto>?) {
        if (!launchDtos.isNullOrEmpty()) {
            db.launchesDao().update(launches = launchDtos.map { it.toEntity() })
        }
    }

}