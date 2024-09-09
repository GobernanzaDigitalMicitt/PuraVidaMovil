package gov.raon.micitt.models.response

import com.google.gson.annotations.SerializedName

data class CheckDocumentStatusRes (
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String,
    @SerializedName("resultData")
    val resultData: CheckDocumentStatusData
)

data class CheckDocumentStatusData (
    @SerializedName("signedEDoc")
    val signedEDoc: String,
)
