package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.model.Company

class CompanyRepository : ICompanyRepository {
    override suspend fun getCompany(): Company? {
        TODO("Not yet implemented")
    }
}