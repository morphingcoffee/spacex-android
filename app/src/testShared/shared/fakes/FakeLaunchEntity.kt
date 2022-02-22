package shared.fakes

import com.morphingcoffee.spacex.data.local.model.LaunchEntity

val FakeSuccessfulLaunchEntity2015 = LaunchEntity(
    uid = "uid1",
    name = "launchname",
    success = true,
    year = 2015,
    dateUtc = "2015-12-22T01:29:01.000Z",
    dateLocal = "2015-12-22T21:29:01-04:00",
    dateUnix = 1450747741,
    links = FakeLaunchLinksEntity,
    rocket = FakeRocketEntity,
)

val FakeSuccessfulLaunchEntity2016 = LaunchEntity(
    uid = "uid2",
    name = "launchname",
    success = true,
    year = 2016,
    dateUtc = "2016-12-22T01:29:01.000Z",
    dateLocal = "2016-12-22T21:29:01-04:00",
    dateUnix = 1482304693,
    links = FakeLaunchLinksEntity,
    rocket = FakeRocketEntity,
)

val FakeFailedLaunchEntity2015 = LaunchEntity(
    uid = "uid3",
    name = "launchname",
    success = false,
    year = 2015,
    dateUtc = "2015-12-22T01:29:00.000Z",
    dateLocal = "2015-12-22T21:29:00-04:00",
    dateUnix = 1450747740,
    links = FakeLaunchLinksEntity,
    rocket = FakeRocketEntity,
)

val FakeFailedLaunchEntity2016 = LaunchEntity(
    uid = "uid4",
    name = "launchname",
    success = false,
    year = 2016,
    dateUtc = "2016-12-22T01:29:00.000Z",
    dateLocal = "2016-12-22T21:29:00-04:00",
    dateUnix = 1482304692,
    links = FakeLaunchLinksEntity,
    rocket = FakeRocketEntity,
)