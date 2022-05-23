package br.com.fiap42scj.crudandroid.presentation.carlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.fiap42scj.crudandroid.R
import br.com.fiap42scj.crudandroid.data.dao.CarsDAO
import br.com.fiap42scj.crudandroid.data.db.CarsDB
import br.com.fiap42scj.crudandroid.databinding.FragmentCarListBinding
import br.com.fiap42scj.crudandroid.presentation.carlist.adapter.CarListAdapter
import br.com.fiap42scj.crudandroid.repository.CarsDatabaseDataSource
import br.com.fiap42scj.crudandroid.repository.CarsRepository
import br.com.fiap42scj.crudandroid.utils.navigateWithAnimations

class CarListFragment : Fragment(R.layout.fragment_car_list) {

    private var _binding: FragmentCarListBinding? = null
    private val binding get() = _binding!!

    //Factory implementado diretamente via viewModels
    private val viewModel: CarListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val carDAO: CarsDAO = CarsDB.getInstance(requireContext()).carsDAO
                val repository: CarsRepository = CarsDatabaseDataSource(carDAO)
                return CarListViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //previne memory leak
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCars()
    }

    private fun observeViewModelEvents() {
        viewModel.allCarsEvent.observe(viewLifecycleOwner){ carList ->
            val carListAdapter = CarListAdapter(carList).apply {
                onItemClick = { cars ->
                    val directions = CarListFragmentDirections
                        .actionCarListFragmentToCarsFragment(cars)
                    findNavController().navigateWithAnimations(directions)
                }
            }

            with(binding.rvCars) {
                setHasFixedSize(true)
                adapter = carListAdapter
            }
        }
    }

    private fun setListeners(){
        binding.fbAddCar.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_carListFragment_to_carsFragment)
        }
    }
}