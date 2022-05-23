package br.com.fiap42scj.crudandroid.presentation.cars

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap42scj.crudandroid.R
import br.com.fiap42scj.crudandroid.repository.CarsRepository
import kotlinx.coroutines.launch

class CarsViewModel(private val repository: CarsRepository) : ViewModel() {

    private val _carStateEventData = MutableLiveData<CarState>()
    val carStateEventData: LiveData<CarState>get() =_carStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>get()= _messageEventData

    private fun includeCar(brand: String, model: String) = viewModelScope.launch {
        try {
            val id = repository.insertCar(brand, model)
            if (id > 0){
                _carStateEventData.value = CarState.Included
                _messageEventData.value = R.string.car_registered
            }
        } catch (ex: Exception){
            _messageEventData.value = R.string.car_error_insert
            Log.e(TAG, ex.toString())
        }
    }

    private fun updateCar(id: Long, brand: String, model: String) = viewModelScope.launch {
        try {
            repository.updateCar(id, brand, model)
            _carStateEventData.value = CarState.Updated
            _messageEventData.value = R.string.car_update_sucess
        }catch (ex: Exception) {
            _messageEventData.value = R.string.car_error_insert
            Log.e(TAG, ex.toString())
        }
    }

    fun includeUpdateCar(brand: String, model: String, id: Long = 0){
        if (id > 0){
            updateCar(id, brand, model)
        } else {
            includeCar(brand, model)
        }
    }

    companion object {
        private val TAG = CarsViewModel::class.java.simpleName
    }

    //MVI
    sealed class CarState {
        object Included : CarState()
        object Updated : CarState()
    }
}