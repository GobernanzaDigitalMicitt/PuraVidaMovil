package gov.raon.micitt.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import gov.raon.micitt.di.DataState
import gov.raon.micitt.di.local.LocalRepoImpl
import gov.raon.micitt.di.local.LocalRepository
import gov.raon.micitt.di.repository.HttpRepository
import gov.raon.micitt.di.repository.http.HttpListener
import gov.raon.micitt.models.AgencyModel
import gov.raon.micitt.models.CheckDocumentModel
import gov.raon.micitt.models.DocumentModel
import gov.raon.micitt.models.SaveDocumentModel
import gov.raon.micitt.models.SignDocumentModel
import gov.raon.micitt.models.realm.RealmDocumentModel
import gov.raon.micitt.models.response.AgencyInfo
import gov.raon.micitt.models.response.AgencyRes
import gov.raon.micitt.models.response.DocumentRes
import gov.raon.micitt.models.response.ErrorRes
import gov.raon.micitt.models.response.SignDocumentRes
import gov.raon.micitt.models.response.SignDocumentStatusRes
import gov.raon.micitt.utils.Log
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val httpRepository: HttpRepository
) : ViewModel() {

    val liveAgencyList = MutableLiveData<MutableList<AgencyInfo>>()

    val liveDocument = MutableLiveData<DocumentRes>()
    val liveSignDocument = MutableLiveData<SignDocumentRes>()
    val liveSignDocumentStatus = MutableLiveData<SignDocumentStatusRes>()

    val liveErrorData = MutableLiveData<ErrorRes>()

    val liveSaveDocumentDataList = MutableLiveData<MutableList<SaveDocumentModel>>()

    fun updateDocument(
        documentModel: DocumentModel,
        strIdentificacion: String,
        agencyName: String,
        eDoc: String,
        date : String
    ) {
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.Main) {
            localRepository.create { realm: Realm ->

                val realmTag = realm.createObject(RealmDocumentModel::class.java)

                realmTag.hashedToken = documentModel.hashedToken
                realmTag.agencyName = agencyName
                realmTag.strIdentificacion = strIdentificacion
                realmTag.agencyCode = documentModel.agencyCode
                realmTag.eDoc = eDoc
                realmTag.dataFormat = documentModel.dataFormat
                realmTag.dataType = documentModel.dataType
                realmTag.nIdType = documentModel.nIdType
                realmTag.date = date

                realm.copyToRealm(realmTag)
            }
        }
    }

    fun getDocumentList(hashedToken: String) {
        val where = LocalRepoImpl.Where()
        where.key = "HashedToken"
        where.value = hashedToken

        CoroutineScope(Dispatchers.IO).launch(Dispatchers.Main) {
            localRepository.selectAll(RealmDocumentModel::class.java) {
                val list = mutableListOf<SaveDocumentModel>()

                it!!.forEach { item ->
                    list.add(SaveDocumentModel(item))
                }

                liveSaveDocumentDataList.postValue(list)
            }
        }
    }

    fun getAgencyList(agencyModel: AgencyModel?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (agencyModel != null) {
                httpRepository.getAgency(agencyModel).collect {
                    when (it) {
                        is DataState.Loading -> {

                        }

                        is DataState.Error -> {
                            Log.i("Request Network Error")
                        }

                        is DataState.Success -> {
                            httpRepository.filterResponse(it.data as Response<*>,
                                HttpListener({ success ->
                                    try {
                                        val data = Gson().fromJson(success.toString(), AgencyRes::class.java)
                                        if (data != null) {
                                            val filteredData = data.resultData.agencyInfoList.filter { it.agencyCode != "0000" && it.agencyCode != "9999" }

                                            liveAgencyList.postValue(filteredData.toMutableList())
                                        }
                                    } catch (e: Exception) {
                                        Log.i("Get Agency Error :: ${e.message}")
                                    }
                                }, { fail ->
                                    try {
                                        val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                        Log.i("Get Agency Server Request Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                        liveErrorData.postValue(errorData)
                                    } catch (e: Exception) {
                                        Log.i("Get Agency Request Error :: ${e.message}")
                                    }
                                })
                            )
                        }
                    }
                }
            }
        }
    }

    fun getDocument(documentModel: DocumentModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.getDocument(documentModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Error -> {
                        Log.i("Request Network Error")
                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data = Gson().fromJson(success.toString(), DocumentRes::class.java)
                                    if (data != null) {
                                        liveDocument.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.i("Document Error :: ${e.message}")
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.i("Document Server Request Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveErrorData.postValue(errorData)
                                } catch (e: Exception) {
                                    Log.i("Document Request Error :: ${e.message}")
                                }
                            })
                        )
                    }
                }
            }
        }
    }

    fun signDocument(signDocumentModel: SignDocumentModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.signDocument(signDocumentModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Error -> {
                        Log.i("Request Network Error")
                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data = Gson().fromJson(success.toString(), SignDocumentRes::class.java)
                                    if (data != null) {
                                        liveSignDocument.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.i("Sign Document Error :: ${e.message}")
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.i("Sign Document Server Request Error  ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveErrorData.postValue(errorData)
                                } catch (e: Exception) {
                                    Log.i("Sign Document Request Error :: ${e.message}")
                                }
                            })
                        )
                    }
                }
            }
        }
    }

    fun checkSignDocumentStatus(checkDocumentModel: CheckDocumentModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.checkSignDocumentStatus(checkDocumentModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Error -> {
                        Log.i("Request Network Error")
                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data = Gson().fromJson(success.toString(), SignDocumentStatusRes::class.java)
                                    if (data != null) {
                                        liveSignDocumentStatus.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.i("Sign Document Status Error :: ${e.message}")
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.i("Sign Document Status Server Request Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveErrorData.postValue(errorData)
                                } catch (e: Exception) {
                                    Log.i("Sign Document Status Request Error :: ${e.message}")
                                }
                            })
                        )
                    }
                }
            }
        }
    }

    fun deleteDocument(saveDocumentModel: SaveDocumentModel) {
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.Main) {
            val where = LocalRepoImpl.Where()
            where.key = "strIdentificacion"
            where.value = saveDocumentModel.strIdentificacion

            localRepository.delete(RealmDocumentModel::class.java, where)

        }
    }

}