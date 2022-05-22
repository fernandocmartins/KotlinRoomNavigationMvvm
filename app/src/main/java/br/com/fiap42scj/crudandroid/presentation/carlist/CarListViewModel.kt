package br.com.fiap42scj.crudandroid.presentation.carlist

import androidx.lifecycle.ViewModel
import br.com.fiap42scj.crudandroid.repository.CarsRepository

class CarListViewModel(private val repository: CarsRepository) : ViewModel() {

    val allCarsEvent = repository.getAllCars()

}