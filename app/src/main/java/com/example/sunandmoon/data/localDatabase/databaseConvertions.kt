package com.example.sunandmoon.data.localDatabase

import android.location.Location
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot
import com.example.sunandmoon.data.util.Shoot

fun storableShootToNormalShoot(storableShoot: StorableShoot): Shoot {
    return Shoot(
        id = storableShoot.uid,
        name = storableShoot.name,
        locationName = storableShoot.locationName,
        location = Location("").apply {
            latitude = storableShoot.latitude
            longitude = storableShoot.longitude
        },
        date = storableShoot.date,
        timeZoneOffset = storableShoot.timeZoneOffset,
        parentProductionId = storableShoot.parentProductionId
    )
}

fun storableShootsToNormalShoots(storableShoots: List<StorableShoot>?): List<Shoot> {
    var shootList = mutableListOf<Shoot>()

    if(storableShoots == null) return shootList

    storableShoots.forEach() { storableShoot ->
        shootList.add(
            storableShootToNormalShoot(storableShoot)
        )
    }

    return shootList
}