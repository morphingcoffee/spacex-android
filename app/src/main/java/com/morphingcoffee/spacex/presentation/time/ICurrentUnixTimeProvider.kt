package com.morphingcoffee.spacex.presentation.time

/**
 * Provides unix timestamp from epoch.
 **/
fun interface ICurrentUnixTimeProvider {
    fun currentTimeMillis(): Long
}