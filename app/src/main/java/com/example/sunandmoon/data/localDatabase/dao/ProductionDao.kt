package com.example.sunandmoon.data.localDatabase.dao

import androidx.room.*
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction

enum class ProductionOrderBy(val value: String) {
    NAME("name"),
    START_DATE_TIME("start_date_time"),
    END_DATE_TIME("end_date_time")
}

//interface for manipulating productions in local database
@Dao
interface ProductionDao {
    // should sort by either start_date_time, end_date_time or name
    // This sorts it such that if it is to be sorted by start_date_time but the start_date_time equals null,
    // then it ends up at the end of the sorting. This makes all of the empty productions be at the end
    @Query("SELECT * FROM production WHERE name LIKE :searchQuery ORDER BY " +
            "(CASE WHEN ((:orderBy = 'start_date_time' AND start_date_time IS NULL) OR (:orderBy = 'end_date_time' AND end_date_time IS NULL)) THEN 1 ELSE 0 END), " +
            "CASE :orderBy " +
            "WHEN 'name' THEN name COLLATE NOCASE " +
            "WHEN 'start_date_time' THEN start_date_time " +
            "WHEN 'end_date_time' THEN end_date_time " +
            "ELSE start_date_time " +
            "END ASC")
    fun getAll(orderBy: String, searchQuery: String): List<StorableProduction>
    //loads a production using
    @Query("SELECT * FROM production WHERE uid = :productionId")
    fun loadById(productionId: Int): StorableProduction

    //inserts a storable shoot in the database
    @Insert
    fun insert(vararg users: StorableProduction)

    //used to delete a production
    @Delete
    fun delete(user: StorableProduction)

    //used to update a production
    @Update
    fun update(production: StorableProduction)

    //used to update date-span of production
    @Query("UPDATE production " +
            "SET start_date_time = (" +
            "    SELECT MIN(strftime('%Y-%m-%dT%H:%M:%fZ', date_time))" +
            "    FROM shoot " +
            "    WHERE parent_production_id = :productionId" +
            "), " +
            "end_date_time = (" +
            "    SELECT MAX(strftime('%Y-%m-%dT%H:%M:%fZ', date_time))" +
            "    FROM shoot" +
            "    WHERE parent_production_id = :productionId" +
            ")" +
            "WHERE uid = :productionId;")
    fun updateDateInterval(productionId: Int)
}
