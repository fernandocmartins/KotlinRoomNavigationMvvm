package br.com.fiap42scj.crudandroid.presentation.carlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap42scj.crudandroid.data.db.CarsEntity
import br.com.fiap42scj.crudandroid.databinding.CarItemBinding

class CarListAdapter(private val carsEntity: List<CarsEntity>): RecyclerView.Adapter<CarListAdapter.CarListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val view = CarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        holder.bindView(carsEntity[position])
    }

    override fun getItemCount() = carsEntity.size

    class CarListViewHolder(val itemBinding: CarItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        private val textViewCarBrand = itemBinding.carName
        private val textViewCarModel = itemBinding.carModel

        fun bindView(carsEntity: CarsEntity){
            textViewCarBrand.text = carsEntity.brand
            textViewCarModel.text = carsEntity.model
        }
    }
}