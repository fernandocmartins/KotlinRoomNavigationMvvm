package br.com.fiap42scj.crudandroid.repository

import androidx.lifecycle.LiveData
import br.com.fiap42scj.crudandroid.data.db.CarsEntity

interface CarsRepository {

    suspend fun insertCar(brand: String, model: String): Long
    suspend fun updateCar(id: Long, brand: String, model: String)
    fun getAllCars(): LiveData<List<CarsEntity>>
    suspend fun deleteCar(id: Long)
    suspend fun deleteAllCars()

}