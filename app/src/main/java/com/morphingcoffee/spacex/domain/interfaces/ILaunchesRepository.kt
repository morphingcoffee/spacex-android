package com.morphingcoffee.spacex.domain.interfaces

import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.SortingOption

interface ILaunchesRepository {
    /** Retrieve launches matching [filterOptions] requirements and sorted by [sortingOption] strategy **/
    suspend fun getLaunches(
        sortingOption: SortingOption,
        filterOptions: List<FilteringOption>
    ): List<Launch>
}