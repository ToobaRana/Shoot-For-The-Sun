package com.example.sunandmoon.data.localDatabase.dao

import androidx.room.*
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction

@Dao
interface ProductionDao {
    @Query("SELECT * FROM production")
    fun getAll(): List<StorableProduction>

    @Query("SELECT * FROM production WHERE uid = :productionId")
    fun loadById(productionId: Int): StorableProduction

    @Insert
    fun insert(vararg users: StorableProduction)

    @Delete
    fun delete(user: StorableProduction)

    @Update
    fun update(production: StorableProduction)
}