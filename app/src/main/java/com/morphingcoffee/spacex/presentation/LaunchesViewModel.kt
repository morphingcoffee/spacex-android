package com.morphingcoffee.spacex.presentation

import androidx.lifecycle.ViewModel
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase

class LaunchesViewModel(private val getLaunchesUseCase: IGetLaunchesUseCase) : ViewModel() {
}