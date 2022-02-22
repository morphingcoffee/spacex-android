package com.morphingcoffee.spacex.shared.fakes

import com.morphingcoffee.spacex.data.local.model.CompanyEntity

val FakeCompanyEntity = CompanyEntity(
    name = "name",
    founded = 2000,
    founder = "founder",
    employees = 12345,
    launchSites = 3,
    valuation = 100000
)