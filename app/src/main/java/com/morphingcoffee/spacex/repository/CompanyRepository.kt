package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.remote.IFetchCompanyService
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.model.Company

class CompanyRepository(private val companyService: IFetchCompanyService) : ICompanyRepository {
    override suspend fun getCompany(): Company? {
        return try {
            val response = companyService.fetchCompany()
            val company = response.body()?.toDomainModel()
            company
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}