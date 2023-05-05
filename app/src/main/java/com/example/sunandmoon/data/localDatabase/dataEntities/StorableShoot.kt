package com.example.sunandmoon.data.localDatabase.dataEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sunandmoon.data.util.Shoot

@Entity(tableName = "shoot")
data class StorableShoot(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "parent_production_id") val parentProductionId: Int?,
    @ColumnInfo(name = "production") val shoot: Shoot?
)
