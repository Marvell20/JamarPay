package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class provisioningResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("message") var message: String
)
