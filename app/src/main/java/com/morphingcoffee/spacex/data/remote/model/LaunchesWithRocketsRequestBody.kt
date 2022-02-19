package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.JsonClass

/**
 * FIXME document
 **/
@JsonClass(generateAdapter = true)
class LaunchesWithRocketsRequestBody(query: RequestQuery? = null, options: RequestOptions? = null) :
    LaunchesRequestBody(query, PopulateRocketRequestOption + options)