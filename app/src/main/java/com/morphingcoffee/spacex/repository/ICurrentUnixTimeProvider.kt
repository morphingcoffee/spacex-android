package com.morphingcoffee.spacex.repository

fun interface ICurrentUnixTimeProvider {
    fun currentTimeMillis(): Long
}