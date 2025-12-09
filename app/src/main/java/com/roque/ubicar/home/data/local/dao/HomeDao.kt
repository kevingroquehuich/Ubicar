package com.roque.ubicar.home.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.roque.ubicar.home.data.local.entity.CarEntity

@Dao
interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarEntity)

    @Query("SELECT * FROM carentity")
    suspend fun getParkedCar(): CarEntity?

    @Delete
    suspend fun deleteCar(car: CarEntity)
}
