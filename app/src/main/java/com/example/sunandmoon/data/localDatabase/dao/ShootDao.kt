package com.example.sunandmoon.data.localDatabase.dao

import androidx.room.*
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot

@Dao
interface ShootDao {
    @Query("SELECT * FROM shoot WHERE parent_production_id IS NULL")
    fun getAllIndependentShoots(): List<StorableShoot>

    @Query("SELECT * FROM shoot WHERE parent_production_id = :productionId")
    fun loadByProductionId(productionId: Int): List<StorableShoot>

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
