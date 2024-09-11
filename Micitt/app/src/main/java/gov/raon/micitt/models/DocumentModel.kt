package gov.raon.micitt.models

import com.google.gson.Gson

class /**/DocumentModel : BaseModel {
    var hashedToken: String
    var agencyCode: String
    var nIdType: String
    var dataFormat: String
    var dataType: String

    constructor(
        hashedToken: String,
        agencyCode: String,
        nIdType: String,
        dataFormat: String,
        dataType: String
    ) {
        this.hashedToken = hashedToken
        this.agencyCode = agencyCode
        this.nIdType = nIdType
        this.dataFormat = dataFormat
        this.dataType = dataType
    }

    override fun fromJson(value: String) {
        val data = Gson().fromJson(value, DocumentModel::class.java)

        hashedToken = data.hashedToken
        agencyCode = data.agencyCode
        nIdType = data.nIdType
        dataFormat = data.dataFormat
        dataType = data.dataType
    }
}
