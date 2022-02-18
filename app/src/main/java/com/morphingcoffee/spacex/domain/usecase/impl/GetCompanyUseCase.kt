package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase.Result

/**
 * Implementation for [com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase].
 */
class GetCompanyUseCase(private val companyRepository: ICompanyRepository) : IGetCompanyUseCase {
    override suspend fun execute(): IGetCompanyUseCase.Result {
        return try {
            val company =
                companyRepository.getCompany() ?: return Result.Error(RuntimeException("No data"))
            return Result.Success(company = company)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }
}