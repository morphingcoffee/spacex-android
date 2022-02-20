package com.morphingcoffee.spacex.domain.interfaces

import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.SortingOption

interface ILaunchesRepository {
    /** Retrieve launches matching [filterStatusOption] & [filterYearOption] requirements
     * and sorted by [sortingOption] strategy.
     * Passing null to [filterStatusOption] and [filterYearOption] disables corresponding filtering.
     **/
    suspend fun getAllLaunches(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?,
    ): List<Launch>
}