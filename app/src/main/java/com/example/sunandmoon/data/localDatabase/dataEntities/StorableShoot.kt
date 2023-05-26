package com.example.sunandmoon.data.localDatabase.dataEntities

import androidx.room.*
import com.example.sunandmoon.data.PreferableWeather
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//used to fetch and store shoots in local database
@Entity(tableName = "shoot")
@TypeConverters(LocalDateTimeConverter::class, PreferredWeatherConverter::class)
data class StorableShoot(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "parent_production_id") val parentProductionId: Int?,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location_name") val locationName: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "date_time") val dateTime: LocalDateTime,
    @ColumnInfo(name = "time_zone_offset") val timeZoneOffset: Double,
    @ColumnInfo(name = "preferred_weather") val preferredWeather: List<PreferableWeather>
)

// this class lets us store LocalDateTime objects in the room database
class LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}

// this class lets us store lists of PreferableWeather in the room database
class PreferredWeatherConverter {
    @TypeConverter
    fun fromPreferredWeather(value: List<PreferableWeather>?): String {
        if(value == null) return ""

        return value.joinToString(",")
    }

    @TypeConverter
    fun toPreferredWeather(value: String?): List<PreferableWeather> {
        if(value == null || value == "") return listOf()

        return value.split(",").map { PreferableWeather.valueOf(it) }
    }
}
