package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.IFetchCompanyService
import com.morphingcoffee.spacex.data.remote.model.CompanyDto
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.model.Company
import java.net.UnknownHostException

class CompanyRepository(private val companyService: IFetchCompanyService, private val db: AppDB) :
    ICompanyRepository {
    override suspend fun getCompany(): Company? {
        return try {
            fetchFromRemote()
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            getFromDb()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun fetchFromRemote(): Company? {
        val response = companyService.fetchCompany()
        val companyDto = response.body()
        updateDb(companyDto)
        return companyDto?.toDomainModel()
    }

    private fun getFromDb(): Company? {
        return db.companyDao().get()?.toDomainModel()
    }

    private fun updateDb(companyDto: CompanyDto?) {
        if (companyDto != null) {
            db.companyDao().insertOrUpdate(company = companyDto.toEntity())
        }
    }

}