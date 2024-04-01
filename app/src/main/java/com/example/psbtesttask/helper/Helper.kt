package com.example.psbtesttask.helper

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.psbtesttask.R
import com.example.psbtesttask.models.ValuteInter
import com.example.psbtesttask.viewmodel.ValuteViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject

object Helper {
    fun isCurrentValuteGTPrevious(item:ValuteInter, reqActivity:FragmentActivity): Int {
        return if (item.Value != null && item.Previous != null){
            if (item.Value!! - item.Previous!! > 0){
                ContextCompat.getColor(reqActivity, R.color.psbGreen)
            }else{
                ContextCompat.getColor(reqActivity, R.color.psbRed)
            }
        }else{
            ContextCompat.getColor(reqActivity, R.color.black)
        }
    }
//    fun isValuteInter(it: Map.Entry<String, JsonElement>, _selectedValute: MutableLiveData<ValuteInter>) {
//        val valuteInter = Gson().fromJson(it.value?.asJsonObject, ValuteInter::class.java)
//        return if (valuteInter.ID == _selectedValute.value?.ID){
//            _selectedValute.value = valuteInter
//        }else{
//       }
//    }


}