package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// TODO options can accept fields like: "limit" "sort" "page"  "pagination"
@JsonClass(generateAdapter = true)
class RequestOptions(
    @Json(name = "populate") val fieldsToPopulate: List<String> = emptyList(),
    @Json(name = "pagination") val pagination: Boolean = false
) {
    operator fun plus(otherOptions: RequestOptions?): RequestOptions {
        if (otherOptions == null) {
            return this
        }
        val populateUnion = fieldsToPopulate.union(otherOptions.fieldsToPopulate).toList()
        return RequestOptions(fieldsToPopulate = populateUnion)
    }
}