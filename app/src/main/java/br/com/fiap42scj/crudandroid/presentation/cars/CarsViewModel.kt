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

    fun includeCar(brand: String, model: String) = viewModelScope.launch {
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

    companion object {
        private val TAG = CarsViewModel::class.java.simpleName
    }

    //MVI
    sealed class CarState {
        object Included : CarState()
    }
}