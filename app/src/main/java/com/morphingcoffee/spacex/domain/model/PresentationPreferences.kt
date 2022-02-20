package com.morphingcoffee.spacex.domain.model

data class PresentationPreferences(
    val sortingOption: SortingOption = SortingOption.Ascending,
    val filteringOptions: List<FilteringOption> = listOf()
)