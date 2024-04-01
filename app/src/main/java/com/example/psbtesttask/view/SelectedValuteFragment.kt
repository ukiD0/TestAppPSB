package com.example.psbtesttask.view


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.psbtesttask.R
import com.example.psbtesttask.databinding.FragmentSelectedValuteBinding
import com.example.psbtesttask.helper.Helper
import com.example.psbtesttask.viewmodel.ValuteViewModel
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * A fragment displaying the current currency.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
class SelectedValuteFragment : Fragment() {

    private lateinit var binding: FragmentSelectedValuteBinding
    private lateinit var valuteViewModel: ValuteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initializing the binding
        binding = FragmentSelectedValuteBinding.inflate(inflater, container, false)
        //Initializing the ViewModel
        valuteViewModel = ViewModelProvider(requireActivity())[ValuteViewModel::class.java]
        //Subscribing to current currency and displaying the data.
        valuteViewModel.selectedValute.observe(viewLifecycleOwner){
            if (it != null){
                with(binding){
                    valuteIDTV.text =       if (it.ID != null)          it.ID else ""
                    valuteNumCodeTV.text =  if (it.NumCode != null)     it.NumCode else ""
                    valuteCharCodeTV.text = if (it.CharCode != null)    it.CharCode else ""
                    valuteNominalTV.text =  if (it.Nominal != null)     it.Nominal.toString() else ""
                    valuteNameTV.text =     if (it.Name != null)        it.Name else ""
                    valuteValueTV.text =    if (it.Value != null)       it.Value.toString() else ""
                    valutePreviousTV.text = if (it.Previous != null)    it.Previous.toString() else ""
                    valuteValueTV.setTextColor(Helper.isCurrentValuteGTPrevious(it, requireActivity()))
                    //Hide the progress bar when data is loaded.
                    progressBarSelectedValuetLoad.isVisible= false
                }
            }
        }
        //Subscribing to current psb data and displaying the data.
        valuteViewModel.selectedPsbData.observe(viewLifecycleOwner){psbData ->
            if (psbData?.Date != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Convert time from string to ZonedDateTime class.
                    val date = ZonedDateTime.parse(psbData.Date.toString())
                    val stringFormat = "${resources.getString(R.string.date_text)} ${date.year}/${date.monthValue}/${date.dayOfMonth}\n" +
                            "${resources.getString(R.string.time_text)} ${date.hour}:${date.minute}"
                    binding.selectedLastDateUpdateTV.text = stringFormat
                    //Hide/show button (Get current currency)
                    binding.currentDayBtn.isVisible = date.toLocalDate() != LocalDate.now()
                }else{
                    binding.selectedLastDateUpdateTV.text = psbData.Date.toString()
                }
                binding.prevBtn.setOnClickListener {
                    //Show the progress bar when data is being loaded.
                    binding.progressBarSelectedValuetLoad.isVisible = true
                    valuteViewModel.getPreviousPSBData(psbData)
                }
            }else{
                with(binding){
                    binding.selectedLastDateUpdateTV.text = resources.getString(R.string.network_err)
                    valuteIDTV.text =       resources.getString(R.string.meow_err)
                    valuteNumCodeTV.text =  resources.getString(R.string.meow_err)
                    valuteCharCodeTV.text = resources.getString(R.string.meow_err)
                    valuteNominalTV.text =  resources.getString(R.string.meow_err)
                    valuteNameTV.text =     resources.getString(R.string.meow_err)
                    valuteValueTV.text =    resources.getString(R.string.meow_err)
                    valutePreviousTV.text = resources.getString(R.string.meow_err)
                    progressBarSelectedValuetLoad.isVisible = true
                }

            }
        }
        //Subscribing to error thrown in the view model and displaying them using toast.
        valuteViewModel.errorMessage.observe(viewLifecycleOwner){
            if (it?.message != null){
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
        //Set an event to the button to get current
        //information on the selected currency.
        binding.currentDayBtn.setOnClickListener {
            //Show the progress bar when data is being loaded.
            binding.progressBarSelectedValuetLoad.isVisible = true
            valuteViewModel.getCurrentPSBData()
        }

        return binding.root
    }

}