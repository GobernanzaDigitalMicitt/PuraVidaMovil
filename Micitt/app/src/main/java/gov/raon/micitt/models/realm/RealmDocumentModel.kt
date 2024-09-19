package gov.raon.micitt.models.realm

import io.realm.RealmObject

open class RealmDocumentModel : RealmObject() {
    var hashedToken: String? = null
    var agencyCode: String? = null
    var nIdType: String? = null
    var dataFormat: String? = null
    var dataType: String? = null
    var agencyName: String? = null
    var eDoc: String? = null
    var strIdentificacion: String? = null
    var date : String? = null
}
