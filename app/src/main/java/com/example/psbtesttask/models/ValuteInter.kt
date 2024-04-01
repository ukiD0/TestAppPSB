package com.example.psbtesttask.models

import com.google.gson.annotations.SerializedName

/**
 * Data class for a currency object.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
data class ValuteInter(
    @SerializedName("ID"       ) var ID       : String? = null,
    @SerializedName("NumCode"  ) var NumCode  : String? = null,
    @SerializedName("CharCode" ) var CharCode : String? = null,
    @SerializedName("Nominal"  ) var Nominal  : Int?    = null,
    @SerializedName("Name"     ) var Name     : String? = null,
    @SerializedName("Value"    ) var Value    : Double? = null,
    @SerializedName("Previous" ) var Previous : Double? = null
)
