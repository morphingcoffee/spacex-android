package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.JsonClass

// TODO query can accept fields like: "date_utc" "$or" "date_precision" "upcoming"  "$text"
@JsonClass(generateAdapter = true)
class RequestQuery()
