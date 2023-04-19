package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class ValidationGoldClientResponse(
    @SerializedName("success") var success: String,
    @SerializedName("data") var data: ValidationGoldClientItem
)
