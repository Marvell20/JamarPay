package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class NextProcessItem(
    @SerializedName("validation_identity") var validation_identity: Boolean,
    @SerializedName("attempts_vi") var attempts_vi: Boolean,
    @SerializedName("provisionamiento") var provisionamiento: Boolean
)