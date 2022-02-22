package com.morphingcoffee.spacex.shared.fakes

import com.morphingcoffee.spacex.data.local.model.LaunchLinksEntity

val FakeLaunchLinksEntity = LaunchLinksEntity(
    youtubeURL = "youtubeURL",
    articleURL = "articleURL",
    wikiURL = "wikiURL",
    patchEntity = FakeLaunchPatchEntity
)