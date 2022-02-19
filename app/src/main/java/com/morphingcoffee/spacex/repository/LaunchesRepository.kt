package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.data.remote.model.LaunchesWithRocketsRequestBody
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.Launch
import java.net.UnknownHostException

class LaunchesRepository(private val launchesService: IFetchLaunchesService) : ILaunchesRepository {
    override suspend fun getLaunches(): List<Launch> {
        return try {
            val paginationResponse = launchesService.fetchLaunchesWithRockets(
                LaunchesWithRocketsRequestBody()
            )
            val paginationDto = paginationResponse.body()
            val launches = paginationDto?.docs
                ?.map { it.toDomainModel() }
                ?.toList()
            // TODO add room DB
            launches ?: emptyList()
        } catch (e: UnknownHostException) {
            TODO("not implemented: db lookup")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}