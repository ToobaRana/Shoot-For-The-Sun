package com.example.sunandmoon.util

//simplifies Location name for display
fun simplifyLocationNameQuery(query: String): String {
    var queryToStore = query
    val splitQuery = queryToStore.split(", ")
    if(splitQuery.size > 2) {
        queryToStore = if(splitQuery.first()[0].isDigit()) {
            listOf(splitQuery.first(), splitQuery[1], splitQuery.last()).joinToString(", ")
        } else {
            listOf(splitQuery.first(), splitQuery.last()).joinToString(", ")
        }
    }
    return queryToStore
}