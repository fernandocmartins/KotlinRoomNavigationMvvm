package br.com.fiap42scj.crudandroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap42scj.crudandroid.data.db.CarsEntity

@Dao
interface CarsDAO {

    @Insert
    suspend fun insert(carsEntity: CarsEntity): Long

    @Update
    suspend fun update(carsEntity: CarsEntity)

    @Query("DELETE FROM cars_table WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM cars_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM cars_table")
    suspend fun getAllCars(): List<CarsEntity>

}