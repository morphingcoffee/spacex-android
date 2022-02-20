package com.morphingcoffee.spacex.domain.model

sealed interface SortingOption {
    object Ascending : SortingOption
    object Descending : SortingOption
}