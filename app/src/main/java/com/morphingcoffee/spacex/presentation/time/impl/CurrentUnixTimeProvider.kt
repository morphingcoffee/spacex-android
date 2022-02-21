package com.morphingcoffee.spacex.presentation.time.impl

import com.morphingcoffee.spacex.presentation.time.ICurrentUnixTimeProvider

class CurrentUnixTimeProvider : ICurrentUnixTimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}