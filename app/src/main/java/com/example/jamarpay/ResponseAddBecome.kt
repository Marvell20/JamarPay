package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class ResponseAddBecome(
    @SerializedName("userid") var userid: Int,
    @SerializedName("success") var success: Boolean,
    @SerializedName("message") var message: String
)
