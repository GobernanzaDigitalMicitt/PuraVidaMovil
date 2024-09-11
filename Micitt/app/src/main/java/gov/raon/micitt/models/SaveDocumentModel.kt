package gov.raon.micitt.models

import com.google.gson.Gson
import gov.raon.micitt.models.realm.RealmDocumentModel
import io.realm.RealmObject

open class SaveDocumentModel : BaseModel {
    var hashedToken: String? = null
    var agencyCode: String? = null
    var nIdType: String? = null
    var dataFormat: String? = null
    var dataType: String? = null
    var agencyName: String? = null
    var eDoc: String? = null
    var strIdentificacion: String? = null

    constructor(realmDocumentModel: RealmDocumentModel) {
        this.hashedToken = realmDocumentModel.hashedToken
        this.agencyCode = realmDocumentModel.agencyCode
        this.agencyName = realmDocumentModel.agencyName
        this.nIdType = realmDocumentModel.nIdType
        this.dataFormat = realmDocumentModel.dataFormat
        this.dataType = realmDocumentModel.dataType
        this.eDoc = realmDocumentModel.eDoc
        this.strIdentificacion = realmDocumentModel.strIdentificacion
    }


    override fun fromJson(value: String) {
        val data = Gson().fromJson(value, SaveDocumentModel::class.java)

        hashedToken = data.hashedToken
        agencyCode = data.agencyCode
        nIdType = data.nIdType
        dataFormat = data.dataFormat
        dataType = data.dataType
        agencyName = data.agencyName
        eDoc = data.eDoc
        strIdentificacion = data.strIdentificacion
    }
}
