package com.morphingcoffee.spacex.domain.interfaces

import com.morphingcoffee.spacex.domain.model.Company

interface ICompanyRepository {
    suspend fun getCompany(): Company?
}