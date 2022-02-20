package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.JsonClass

/**
 * FIXME document
 **/
@JsonClass(generateAdapter = true)
class LaunchesWithRocketsRequestBody(
    query: RequestQuery? = null,
    options: RequestOptions = RequestOptions()
) : LaunchesRequestBody(query, options.populateWith(PopulateRequestFields.Rocket))