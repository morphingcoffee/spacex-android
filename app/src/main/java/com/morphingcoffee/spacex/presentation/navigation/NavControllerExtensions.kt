package com.morphingcoffee.spacex.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDirections

/**
 * Ensures only an existing nav action can be performed.
 * Otherwise, clicking a button destined to open a dialog fast multiple times can cause exceptions.
 **/
fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}