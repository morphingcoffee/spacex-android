package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase.Result

/**
 * Use case for getting a [com.morphingcoffee.spacex.domain.model.Company] from the repository layer.
 * Here we assume that the business requirement dictates that successful lookup must not yield a null result.
 * Hence, we treat null result scenario as a failure.
 */
class GetCompanyUseCase(private val companyRepository: ICompanyRepository) : IGetCompanyUseCase {
    override suspend fun execute(): IGetCompanyUseCase.Result {
        return try {
            val company =
                companyRepository.getCompany() ?: return Result.Error(RuntimeException("No data"))
            return Result.Success(company = company)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}