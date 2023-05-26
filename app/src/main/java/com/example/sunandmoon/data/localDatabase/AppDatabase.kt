package com.example.sunandmoon.data.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sunandmoon.data.localDatabase.dao.ProductionDao
import com.example.sunandmoon.data.localDatabase.dao.ShootDao
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableProduction
import com.example.sunandmoon.data.localDatabase.dataEntities.StorableShoot


//specifies the setup, and provides access to the underlying database
@Database(entities = [StorableShoot::class, StorableProduction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shootDao(): ShootDao
    abstract fun productionDao(): ProductionDao
}
