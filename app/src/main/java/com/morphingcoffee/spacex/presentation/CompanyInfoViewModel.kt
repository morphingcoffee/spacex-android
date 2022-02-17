package com.morphingcoffee.spacex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import kotlinx.coroutines.launch

class CompanyInfoViewModel(private val getCompanyUseCase: IGetCompanyUseCase) : ViewModel() {

    private val _company: MutableLiveData<Company> = MutableLiveData()
    val company: LiveData<Company>
        get() = _company

    init {
        viewModelScope.launch {
            getCompanyUseCase.execute().also {
                this@CompanyInfoViewModel::handleCompany
            }
        }
    }

    private fun handleCompany(new: Company) {

    }

}