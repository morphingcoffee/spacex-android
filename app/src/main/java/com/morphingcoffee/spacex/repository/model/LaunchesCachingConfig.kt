package com.morphingcoffee.spacex.repository.model

/**
 * [LaunchesCachingConfig] defines a policy of minimum time to elapse before fetching data from
 * remote data source again. While still within a valid cache time window, use DB data instead.
 **/
data class LaunchesCachingConfig(val launchesCacheValidityInMillis: Long)
