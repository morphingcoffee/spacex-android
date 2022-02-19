package com.morphingcoffee.spacex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaunchesViewModel(
    private val getLaunchesUseCase: IGetLaunchesUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    sealed interface UiState {
        object Loading : UiState
        data class Display(val launches: List<Launch>) : UiState
        data class Error(val errorRes: Int) : UiState
    }

    private val _state: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val state: LiveData<UiState>
        get() = _state

    init {
        viewModelScope.launch(defaultDispatcher) {
            getLaunchesUseCase.execute().also(
                this@LaunchesViewModel::handleLaunchesResult
            )
        }
    }

    private fun handleLaunchesResult(result: IGetLaunchesUseCase.Result) {
        when (result) {
            is IGetLaunchesUseCase.Result.Error -> _state.postValue(UiState.Error(R.string.unknown_error_message))
            is IGetLaunchesUseCase.Result.Success -> _state.postValue(UiState.Display(result.launches))
        }
    }
}