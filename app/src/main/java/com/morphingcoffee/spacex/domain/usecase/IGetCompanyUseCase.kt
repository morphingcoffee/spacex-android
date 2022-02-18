package com.morphingcoffee.spacex.domain.usecase

import com.morphingcoffee.spacex.domain.model.Company

/**
 * Use case for getting a [com.morphingcoffee.spacex.domain.model.Company] from the repository layer.
 * Here we assume that the business requirement dictates that successful lookup must not yield a null result.
 * Hence, we treat null result scenario as a failure.
 */
interface IGetCompanyUseCase {
    suspend fun execute(): Result
    sealed interface Result {
        data class Success(val company: Company) : Result
        data class Error(val t: Throwable) : Result
    }
}