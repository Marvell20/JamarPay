package com.example.jamarpay

import com.google.gson.annotations.SerializedName

data class DocumentTypeResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("data") var data: List<DocumentTypeItem>
)
