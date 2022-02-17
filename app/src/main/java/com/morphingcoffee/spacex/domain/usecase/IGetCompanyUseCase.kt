package com.morphingcoffee.spacex.domain.usecase

import com.morphingcoffee.spacex.domain.model.Company

interface IGetCompanyUseCase {
    suspend fun execute(): Result
    sealed interface Result {
        data class Success(val company: Company) : Result
        data class Error(val t: Throwable) : Result
    }
}