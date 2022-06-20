package com.MovieDb.app.core.data.source.remote.entity

class AiredDateEntity {
    val startDate: String
    val endDate: String

    constructor(startDate: String, endDate: String) {
        this.startDate = startDate
        this.endDate = endDate
    }

    constructor(date: String) {
        startDate = date
        endDate = date
    }
}