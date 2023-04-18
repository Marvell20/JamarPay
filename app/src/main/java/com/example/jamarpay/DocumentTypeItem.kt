package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class DocumentTypeItem(
    @SerializedName("code") var code: String,
    @SerializedName("label") var label: String
)
