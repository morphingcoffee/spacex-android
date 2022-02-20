package com.morphingcoffee.spacex.repository

class CurrentUnixTimeProvider : ICurrentUnixTimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}