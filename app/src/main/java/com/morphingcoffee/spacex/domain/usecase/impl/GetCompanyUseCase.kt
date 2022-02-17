package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase

class GetCompanyUseCase(private val companyRepository: ICompanyRepository) : IGetCompanyUseCase {
    override suspend fun execute(): Company? = companyRepository.getCompany()
}