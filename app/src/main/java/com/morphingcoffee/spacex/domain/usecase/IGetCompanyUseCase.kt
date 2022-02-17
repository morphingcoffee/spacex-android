package com.morphingcoffee.spacex.domain.usecase

import com.morphingcoffee.spacex.domain.model.Company

interface IGetCompanyUseCase {
    suspend fun execute(): Company?
}