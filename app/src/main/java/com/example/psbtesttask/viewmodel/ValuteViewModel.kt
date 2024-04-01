package com.example.psbtesttask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psbtesttask.models.PSBMain
import com.example.psbtesttask.models.ValuteInter
import com.example.psbtesttask.repositroy.Common
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This is a ViewModel class for working with currencies.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
class ValuteViewModel : ViewModel() {
    companion object{
        //Constant for the server request timer.
        const val TIME_CALL_REPEAT:Long = 30000
    }
    //Private variables for dynamic work with data within a class.
    private var _valutes =          MutableLiveData<List<ValuteInter>>()
    private var _psbData =          MutableLiveData<PSBMain>()
    private var _selectedPsbData =  MutableLiveData<PSBMain>()
    private var _errorMessage =     MutableLiveData<Throwable>()
    private var _selectedValute =   MutableLiveData<ValuteInter>()
    //Retrofit Service
    private var mService =          Common.retrofitService
    //External variables for subscribing to them outside the class.
    val selectedValute:     LiveData<ValuteInter> =         _selectedValute
    val valutes:            LiveData<List<ValuteInter>> =   _valutes
    val psbData:            LiveData<PSBMain> =             _psbData
    val selectedPsbData:    LiveData<PSBMain> =             _selectedPsbData
    val errorMessage:       LiveData<Throwable> =           _errorMessage
    /**
     * This function start the process (one-time when init view model)
     * of querying the server every 30 seconds.
     *
     */
    init {
        viewModelScope.launch {
            try {
                //Perform requests to the server only when the current application context is active.
                //(Correctly handle situations when the application is minimized.)
                while (currentCoroutineContext().isActive){
                    try {
                        mService.getPSBData().enqueue(object : Callback<PSBMain> {
                            override fun onResponse(call: Call<PSBMain>, response: Response<PSBMain>) {
                                try {
                                    //Get response body
                                    val body = response.body()
                                    //Set psb data
                                    _psbData.value = body!!
                                    if (_selectedPsbData.value?.Date == null){
                                        _selectedPsbData.value = _psbData.value
                                    }
                                    //Converting a JSON object into an array of classes for ease of use.
                                    val arrValuteInter : ArrayList<ValuteInter> = arrayListOf()
                                    body.Valute!!.asMap().map {
                                        val valuteInter = Gson().fromJson(it.value.asJsonObject, ValuteInter::class.java)
                                        if (valuteInter.ID == _selectedValute.value?.ID){
                                            _selectedValute.value = valuteInter
                                        }
                                        arrValuteInter.add(valuteInter)
                                    }
                                    //Set list currency
                                    _valutes.value = arrValuteInter
                                }catch (e:Exception){
                                    excep(e)
                                }
                            }
                            override fun onFailure(call: Call<PSBMain>, t: Throwable) {
                                excep(t)
                            }
                        })
                    }catch (e: Exception){
                        excep(e)
                    }
                    //Set timer
                    delay(TIME_CALL_REPEAT)
                }
            }catch (e:Exception){
                excep(e)
            }
        }
    }
    /**
     * This function set current currency and psb data.
     *
     * @param valute Current currency.
     */
    fun setSelectedValute(valute: ValuteInter){
        try {
            _selectedValute.value = valute
            _selectedPsbData.value = _psbData.value
        }catch (e:Exception){
            excep(e)
        }
    }
    /**
     * This function get data for the current date.
     *
     */
    fun getCurrentPSBData(){
        try {
            mService.getPSBData().enqueue(object : Callback<PSBMain>{
                override fun onResponse(call: Call<PSBMain>, response: Response<PSBMain>) {
                    try {
                        val body = response.body()
                        _selectedPsbData.value = body!!
                        body.Valute!!.asMap().map {
                            val valuteInter = Gson().fromJson(it.value.asJsonObject, ValuteInter::class.java)
                            if (valuteInter.ID == _selectedValute.value?.ID){
                                _selectedValute.value = valuteInter
                            }
                        }
                    }catch (e:Exception){
                        excep(e)
                    }
                }
                override fun onFailure(call: Call<PSBMain>, t: Throwable) {
                    excep(t)
                }
            })
        }catch (e:Exception){
            excep(e)
        }

    }
    /**
     * This function takes the current data and based on the reference gets the previous data.
     *
     * @param psbData Current data.
     */
    fun getPreviousPSBData(psbData:PSBMain){
        try {
            val prevUrl = psbData.PreviousURL.toString().replace("//www.cbr-xml-daily.ru/", "")
            mService.getArchiveData(prevUrl).enqueue(object : Callback<PSBMain>{
                override fun onResponse(call: Call<PSBMain>, response: Response<PSBMain>) {
                    try {
                        val body = response.body()
                        _selectedPsbData.value = body!!
                        body.Valute!!.asMap().map {
                            val valuteInter = Gson().fromJson(it.value.asJsonObject, ValuteInter::class.java)
                            if (valuteInter.ID == _selectedValute.value?.ID){
                                _selectedValute.value = valuteInter
                            }
                        }
                    }catch (e:Exception){
                        excep(e)
                    }
                }
                override fun onFailure(call: Call<PSBMain>, t: Throwable) {
                    excep(t)
                }
            })
        }catch (e:Exception){
            excep(e)
        }
    }
    /**
     * This function resets our variables in case of an error or Internet loss.
     *
     * @param e Exception or Throwable
     * */
    private fun excep(e:Throwable){
        _errorMessage.value = e
        _psbData.value = PSBMain()
        _valutes.value = arrayListOf()
        _selectedPsbData.value = PSBMain()
        _errorMessage.value = Exception()
    }

}