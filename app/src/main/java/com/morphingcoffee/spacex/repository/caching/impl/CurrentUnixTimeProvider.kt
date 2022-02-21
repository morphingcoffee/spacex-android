package com.morphingcoffee.spacex.repository.caching.impl

import com.morphingcoffee.spacex.repository.caching.ICurrentUnixTimeProvider

class CurrentUnixTimeProvider : ICurrentUnixTimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}