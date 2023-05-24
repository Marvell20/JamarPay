package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class ProvisioningItem(
    @SerializedName("c_emp") var c_emp: String,
    @SerializedName("n_ide") var n_ide: String
)
