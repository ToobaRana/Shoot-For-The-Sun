package com.example.sunandmoon.data.localDatabase.dao

import androidx.room.*
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot

enum class ShootOrderBy(val value: String) {
    NAME("name"),
    DATE_TIME("date_time"),
    LATITUDE("latitude"),
    LONGITUDE("longitude")
}

@Dao
interface ShootDao {
    // should sort by either latitude, date_time, latitude or longitude
    @Query("SELECT * FROM shoot WHERE parent_production_id IS NULL AND name LIKE :searchQuery ORDER BY CASE :orderBy " +
            "WHEN 'name' THEN name " +
            "WHEN 'date_time' THEN date_time " +
            "WHEN 'latitude' THEN latitude " +
            "WHEN 'longitude' THEN longitude " +
            "ELSE date_time " +
            "END ASC")
    fun getAllSoloShoots(orderBy: String, searchQuery: String): List<StorableShoot>

    @Query("SELECT * FROM shoot WHERE parent_production_id = :productionId AND name LIKE :searchQuery ORDER BY CASE :orderBy " +
            "WHEN 'name' THEN name " +
            "WHEN 'date_time' THEN date_time " +
            "WHEN 'latitude' THEN latitude " +
            "WHEN 'longitude' THEN longitude " +
            "ELSE date_time " +
            "END ASC")
    fun loadByProductionId(productionId: Int, orderBy: String, searchQuery: String): List<StorableShoot>

    @Query("SELECT * FROM shoot WHERE uid = :shootId")
    fun loadById(shootId: Int): StorableShoot

    @Insert
    fun insert(vararg users: StorableShoot)

    @Delete
    fun delete(user: StorableShoot)

    @Query("DELETE FROM shoot WHERE parent_production_id = :productionId")
    fun deleteShootsInProduction(productionId: Int)

    @Update
    fun update(shoot: StorableShoot)
}