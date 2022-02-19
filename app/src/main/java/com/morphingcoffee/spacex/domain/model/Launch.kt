package com.morphingcoffee.spacex.domain.model

data class Launch(
    val id: String,
    val name: String?,
    // TODO change this to some date object?
    val launchDateTime: String?,
    val launchStatus: LaunchStatus,
    val rocket: Rocket?,
    val links: Links?,
)