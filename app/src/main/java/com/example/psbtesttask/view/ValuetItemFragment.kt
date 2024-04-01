package com.example.psbtesttask.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.psbtesttask.R
import com.example.psbtesttask.adapters.MyValuetItemRecyclerViewAdapter
import com.example.psbtesttask.databinding.FragmentValuteListBinding
import com.example.psbtesttask.viewmodel.ValuteViewModel
import java.time.ZonedDateTime

/**
 * A fragment displaying the list of currencies.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
class ValuetItemFragment : Fragment() {
    private lateinit var valuteViewModel:ValuteViewModel
    private lateinit var binding: FragmentValuteListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initializing the binding
        binding = FragmentValuteListBinding.inflate(inflater, container, false)
        //Initializing the ViewModel
        valuteViewModel = ViewModelProvider(requireActivity())[ValuteViewModel::class.java]
        //Creating a LinearLayoutManager for list of currencies
        val manager = LinearLayoutManager(requireContext())
        //Subscribing to PSB data and displaying the current date.
        valuteViewModel.psbData.observe(viewLifecycleOwner){
            if (it?.Date != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val date = ZonedDateTime.parse(it.Date.toString())
                    val stringFormat = "${resources.getString(R.string.date_text)} ${date.year}/${date.monthValue}/${date.dayOfMonth}\n" +
                            "${resources.getString(R.string.time_text)} ${date.hour}:${date.minute}"
                    binding.lastDateUpdateTV.text = stringFormat
                }else{
                    binding.lastDateUpdateTV.text = it.Date.toString()
                }
            }
        }
        //Subscribing to a list of currencies and transferring this list to the recycling view adapter.
        valuteViewModel.valutes.observe(viewLifecycleOwner){
            if(it != null){
                binding.progressBarValuetLoad.isVisible = it.isEmpty()
                val adapter = MyValuetItemRecyclerViewAdapter(it, requireActivity())
                binding.listValutes.layoutManager = manager
                binding.listValutes.adapter = adapter
            }
        }
        //Subscribing to errors thrown in the view model and displaying them using toast.
        valuteViewModel.errorMessage.observe(viewLifecycleOwner){
            if (it?.message != null){
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

}