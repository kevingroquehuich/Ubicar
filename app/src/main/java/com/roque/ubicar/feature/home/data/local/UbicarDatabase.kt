package com.roque.ubicar.feature.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roque.ubicar.feature.home.data.local.dao.HomeDao
import com.roque.ubicar.feature.home.data.local.entity.CarEntity

@Database(
    entities = [CarEntity::class],
    version = 2,
    exportSchema = false
)
abstract class UbicarDatabase : RoomDatabase() {
    abstract val dao: HomeDao


}