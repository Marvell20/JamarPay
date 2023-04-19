package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class ValidationGoldClientItem(
    @SerializedName("message") var message: String,
    @SerializedName("segmento") var segmento: String,
)
