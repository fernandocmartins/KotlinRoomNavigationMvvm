package br.com.fiap42scj.crudandroid.presentation.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.fiap42scj.crudandroid.R
import br.com.fiap42scj.crudandroid.data.dao.CarsDAO
import br.com.fiap42scj.crudandroid.data.db.CarsDB
import br.com.fiap42scj.crudandroid.databinding.FragmentCarsBinding
import br.com.fiap42scj.crudandroid.repository.CarsDatabaseDataSource
import br.com.fiap42scj.crudandroid.repository.CarsRepository
import br.com.fiap42scj.crudandroid.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class CarsFragment : Fragment(R.layout.fragment_cars) {

    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!

    //Factory implementado diretamente via viewModels
    private val viewModel: CarsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val carDAO: CarsDAO = CarsDB.getInstance(requireContext()).carsDAO
                val repository: CarsRepository = CarsDatabaseDataSource(carDAO)
                return CarsViewModel(repository) as T
            }
        }
    }

    private val args: CarsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //previne memory leak
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.cars?.let {carsEntity ->  
            binding.registerCarBtn.text = getString(R.string.update)
            binding.edtCarBrand.setText(carsEntity.brand)
            binding.edtCarModel.setText(carsEntity.model)
        }

        observerEvents()
        initListeners()
    }

    private fun observerEvents() {
        viewModel.carStateEventData.observe(viewLifecycleOwner) { carState ->
            when(carState){
                is CarsViewModel.CarState.Included -> {
                    clearEditTexts()
                    hideKeyBoard()
                    findNavController().popBackStack()
                }
                is CarsViewModel.CarState.Updated -> {
                    clearEditTexts()
                    hideKeyBoard()
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.ANIMATION_MODE_SLIDE).show()
        }
    }

    private fun clearEditTexts() {
        binding.edtCarBrand.text?.clear()
        binding.edtCarModel.text?.clear()
    }

    private fun hideKeyBoard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard()
        }
    }

    private fun initListeners() {
        binding.registerCarBtn.setOnClickListener {
            val brand = binding.edtCarBrand.text.toString()
            val model = binding.edtCarModel.text.toString()

            viewModel.includeUpdateCar(brand, model, args.cars?.id ?: 0)
        }
    }
}