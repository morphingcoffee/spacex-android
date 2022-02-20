package com.morphingcoffee.spacex.domain.model

sealed interface FilteringOption {
    data class ByLaunchStatus(val status: LaunchStatus) : FilteringOption
    data class ByYears(val years: List<Int>) : FilteringOption
}