package com.morphingcoffee.spacex.shared.fakes

import com.morphingcoffee.spacex.data.remote.model.LaunchLinksDto

val FakeLaunchLinksDto = LaunchLinksDto(
    youtubeURL = "youtubeURL",
    articleURL = "articleURL",
    wikiURL = "wikiURL",
    patchDto = FakeLaunchPatchDto
)