package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class ProvisioningResponse(
    @SerializedName("success") var success: String,
    @SerializedName("message") var message: String
)
