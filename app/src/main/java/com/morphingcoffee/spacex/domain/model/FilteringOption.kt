package com.morphingcoffee.spacex.domain.model

sealed interface FilteringOption {
    data class ByLaunchStatus(val status: LaunchStatus) : FilteringOption
    data class ByYear(val year: Int) : FilteringOption
}