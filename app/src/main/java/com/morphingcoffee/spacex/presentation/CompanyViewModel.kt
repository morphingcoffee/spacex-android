package com.morphingcoffee.spacex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.domain.model.Company
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompanyViewModel(
    private val getCompanyUseCase: IGetCompanyUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    sealed interface UiState {
        object Loading : UiState
        data class Display(val company: Company) : UiState
        data class Error(val errorRes: Int) : UiState
    }

    private val _state: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val state: LiveData<UiState>
        get() = _state

    init {
        viewModelScope.launch(defaultDispatcher) {
            getCompanyUseCase.execute().also(
                this@CompanyViewModel::handleCompanyResult
            )
        }
    }

    private fun handleCompanyResult(result: IGetCompanyUseCase.Result) {
        when (result) {
            is IGetCompanyUseCase.Result.Error -> _state.postValue(UiState.Error(R.string.unknown_error_message))

            is IGetCompanyUseCase.Result.Success -> _state.postValue(UiState.Display(result.company))
        }
    }

}