package com.morphingcoffee.spacex.domain.usecase

import com.morphingcoffee.spacex.domain.model.Launch

interface IGetLaunchesUseCase {
    suspend fun execute(): Result
    sealed interface Result {
        data class Success(val launches: List<Launch>) : Result
        data class Error(val t: Throwable) : Result
    }
}