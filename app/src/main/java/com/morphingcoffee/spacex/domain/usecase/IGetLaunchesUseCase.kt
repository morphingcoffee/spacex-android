package com.morphingcoffee.spacex.domain.usecase

import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.SortingOption

/**
 * Use case for getting [com.morphingcoffee.spacex.domain.model.Launch] instances from the repository layer.
 */
interface IGetLaunchesUseCase {
    /**
     * Retrieve the list of launches.
     * If [sortingOption] is not supplied, items are ordered from newest to oldest (descending) by default
     * If [filterStatusOption] & [filterYearOption] are not supplied, no launches filtering is applied.
     **/
    suspend fun execute(
        sortingOption: SortingOption = SortingOption.Descending,
        filterStatusOption: FilteringOption.ByLaunchStatus? = null,
        filterYearOption: FilteringOption.ByYear? = null
    ): Result

    sealed interface Result {
        data class Success(val launches: List<Launch>) : Result

        data class Error(val t: Throwable) : Result
    }
}