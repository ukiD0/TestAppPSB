package com.example.psbtesttask.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * Data class for a daily json object.
 *
 * @author Valentina Korovkina
 * Created 30/03/2024
 */
data class PSBMain(
    @SerializedName("Date"         ) var Date         : String? = null,
    @SerializedName("PreviousDate" ) var PreviousDate : String? = null,
    @SerializedName("PreviousURL"  ) var PreviousURL  : String? = null,
    @SerializedName("Timestamp"    ) var Timestamp    : String? = null,
    @SerializedName("Valute"       ) var Valute       : JsonObject? = null
)
