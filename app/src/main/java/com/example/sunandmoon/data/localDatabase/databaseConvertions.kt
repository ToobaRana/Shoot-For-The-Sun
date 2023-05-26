package com.example.sunandmoon.data.localDatabase

import android.location.Location
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import com.example.sunandmoon.data.dataUtil.Shoot

//converts a single shoot fetched from database to a runtime-storeable shoot and returns it
fun storableShootToNormalShoot(storableShoot: StorableShoot): Shoot {
    return Shoot(
        id = storableShoot.uid,
        name = storableShoot.name,
        locationName = storableShoot.locationName,
        location = Location("").apply {
            latitude = storableShoot.latitude
            longitude = storableShoot.longitude
        },
        dateTime = storableShoot.dateTime,
        timeZoneOffset = storableShoot.timeZoneOffset,
        parentProductionId = storableShoot.parentProductionId,
        preferredWeather = storableShoot.preferredWeather
    )
}
//converts shoots stored during runtime to database-storeable shoots and returns them as a list
fun storableShootsToNormalShoots(storableShoots: List<StorableShoot>?): List<Shoot> {
    val shootList = mutableListOf<Shoot>()

    if(storableShoots == null) return shootList

    storableShoots.forEach() { storableShoot ->
        shootList.add(
            storableShootToNormalShoot(storableShoot)
        )
    }

    return shootList
}