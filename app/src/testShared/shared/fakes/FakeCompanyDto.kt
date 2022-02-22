package shared.fakes

import com.morphingcoffee.spacex.data.remote.model.CompanyDto

val FakeCompanyDto = CompanyDto(
    name = "name",
    founded = 2000,
    founder = "founder",
    employees = 12345,
    launchSites = 3,
    valuation = 100000
)