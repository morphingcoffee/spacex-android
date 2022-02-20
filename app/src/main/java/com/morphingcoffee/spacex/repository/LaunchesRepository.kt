package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.toDomainModel
import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.data.remote.model.LaunchWithRocketDto
import com.morphingcoffee.spacex.data.remote.model.LaunchesWithRocketsRequestBody
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.Launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class LaunchesRepository(
    private val launchesService: IFetchLaunchesService,
    private val db: AppDB
) : ILaunchesRepository {
    override suspend fun getLaunches(): List<Launch> {
        return try {
            val paginationResponse = launchesService.fetchLaunchesWithRockets(
                LaunchesWithRocketsRequestBody()
            )
            val paginationDto = paginationResponse.body()
            val launchDtos = paginationDto?.docs
            updateDb(launchDtos)
            val launches = launchDtos
                ?.map { it.toDomainModel() }
                ?.toList()
            launches ?: emptyList()
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            db.launchesDao().getAll()
                ?.map { it.toDomainModel() }
                ?.toList()
                ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun updateDb(launchDtos: List<LaunchWithRocketDto>?) {
        if (!launchDtos.isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                db.launchesDao().update(launches = launchDtos.map { it.toEntity() })
            }
        }
    }

}