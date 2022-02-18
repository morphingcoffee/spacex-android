package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.local.toDomainModel
import com.morphingcoffee.spacex.data.remote.IFetchCompanyService
import com.morphingcoffee.spacex.data.remote.model.CompanyDto
import com.morphingcoffee.spacex.data.remote.model.toDomainModel
import com.morphingcoffee.spacex.data.remote.model.toEntity
import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.model.Company
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class CompanyRepository(private val companyService: IFetchCompanyService, private val db: AppDB) :
    ICompanyRepository {
    override suspend fun getCompany(): Company? {
        return try {
            val response = companyService.fetchCompany()
            val companyDto = response.body()
            updateDb(companyDto)
            val company = companyDto?.toDomainModel()
            company
        } catch (e: UnknownHostException) {
            // No Internet connection case. Look-up cached values from DB
            db.companyDao().get()?.toDomainModel()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun updateDb(companyDto: CompanyDto?) {
        if (companyDto != null) {
            withContext(Dispatchers.IO) {
                db.companyDao().update(company = companyDto.toEntity())
            }
        }
    }

}