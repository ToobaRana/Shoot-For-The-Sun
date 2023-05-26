package com.example.sunandmoon.data.localDatabase.dao

import androidx.room.*
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot

//order shoots by value (NAME/DATE_TIME/LATITUDE/LONGITUDE
enum class ShootOrderBy(val value: String) {
    NAME("name"),
    DATE_TIME("date_time"),
    LATITUDE("latitude"),
    LONGITUDE("longitude")
}
//interface for manipulating shoots in local database
@Dao
interface ShootDao {
    //fetches all solo shoots
    // should sort by either latitude, date_time, latitude or longitude
    @Query("SELECT * FROM shoot WHERE parent_production_id IS NULL AND name LIKE :searchQuery ORDER BY CASE :orderBy " +
            "WHEN 'name' THEN name COLLATE NOCASE " +
            "WHEN 'date_time' THEN date_time " +
            "WHEN 'latitude' THEN latitude " +
            "WHEN 'longitude' THEN longitude " +
            "ELSE date_time " +
            "END ASC")
    fun getAllSoloShoots(orderBy: String, searchQuery: String): List<StorableShoot>

    //loads shoots with a specified production
    @Query("SELECT * FROM shoot WHERE parent_production_id = :productionId AND name LIKE :searchQuery ORDER BY CASE :orderBy " +
            "WHEN 'name' THEN name COLLATE NOCASE " +
            "WHEN 'date_time' THEN date_time " +
            "WHEN 'latitude' THEN latitude " +
            "WHEN 'longitude' THEN longitude " +
            "ELSE date_time " +
            "END ASC")
    fun loadByProductionId(productionId: Int, orderBy: String, searchQuery: String): List<StorableShoot>

    //loads a shoot using id
    @Query("SELECT * FROM shoot WHERE uid = :shootId")
    fun loadById(shootId: Int): StorableShoot

    //inserts a storeable shoot to the database
    @Insert
    fun insert(vararg users: StorableShoot)

    //deletes a shoot from the database
    @Delete
    fun delete(user: StorableShoot)

    //deletes all shoots with a specified productionId
    @Query("DELETE FROM shoot WHERE parent_production_id = :productionId")
    fun deleteShootsInProduction(productionId: Int)

    //updates specified shoot
    @Update
    fun update(shoot: StorableShoot)
}