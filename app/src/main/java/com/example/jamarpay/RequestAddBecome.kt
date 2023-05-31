package com.example.jamarpay

import com.google.gson.JsonNull
import com.google.gson.annotations.SerializedName

data class RequestAddBecome(
    @SerializedName("n_ide") var n_ide: String,
    @SerializedName("c_emp") var c_emp: String,
    @SerializedName("uuid") var uuid: String,
    @SerializedName("response_json") var response_json: String,
    @SerializedName("status") var status: String,
    @SerializedName("result_status") var result_status: String
)
