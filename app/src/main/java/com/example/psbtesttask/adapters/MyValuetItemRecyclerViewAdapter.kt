package com.example.psbtesttask.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.psbtesttask.R
import com.example.psbtesttask.databinding.FragmentValuteItemBinding
import com.example.psbtesttask.helper.Helper
import com.example.psbtesttask.models.ValuteInter
import com.example.psbtesttask.viewmodel.ValuteViewModel

/**
 * Recycling view adapter for displaying a list of currencies.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
class MyValuetItemRecyclerViewAdapter(
    private val values: List<ValuteInter>,
    private val reqActivity: FragmentActivity
) : RecyclerView.Adapter<MyValuetItemRecyclerViewAdapter.ViewHolder>() {
    //Creating a view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentValuteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }
    //A function for connecting list item layout and array item data.
    override fun onBindViewHolder(valuetHolder: ViewHolder, position: Int) {
        val item = values[position]
        valuetHolder.value.setTextColor(Helper.isCurrentValuteGTPrevious(item, reqActivity))
        valuetHolder.name.text = item.Name.toString()
        valuetHolder.value.text = item.Value.toString()
        valuetHolder.container.setOnClickListener {
            valuetHolder.valuteViewModel.setSelectedValute(item)
            Navigation.findNavController(valuetHolder.container).navigate(R.id.action_valuetItemFragment_to_selectedValuteFragment)
        }
    }
    //Get item count
    override fun getItemCount(): Int = values.size
    //Inner class for binding layout elements.
    inner class ViewHolder(binding: FragmentValuteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val valuteViewModel = ViewModelProvider(reqActivity)[ValuteViewModel::class.java]
        val name: TextView = binding.itemValuteName
        val value: TextView = binding.itemValuteValue
        val container: CardView = binding.containerValuteItem
    }

}