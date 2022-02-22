package shared.fakes

import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus

val FakeLaunch = Launch(
    id = "launchid",
    name = "launchname",
    launchDateTime = FakeDateTime,
    launchStatus = LaunchStatus.Failed,
    rocket = FakeRocket,
    links = FakeLinks,
)