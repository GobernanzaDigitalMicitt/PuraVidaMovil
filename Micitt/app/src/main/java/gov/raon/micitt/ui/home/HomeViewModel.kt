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
    val liveErrorAgencyList = MutableLiveData<String>()

    val liveDocument = MutableLiveData<DocumentRes>()
    val liveSignDocument = MutableLiveData<SignDocumentRes>()
    val liveSignDocumentStatus = MutableLiveData<SignDocumentStatusRes>()

    val liveErrorDocument = MutableLiveData<String>()

    val liveSaveDocumentDataList = MutableLiveData<MutableList<SaveDocumentModel>>()

    fun updateDocument(
        documentModel: DocumentModel,
        strIdentificacion: String,
        agencyName: String,
        eDoc: String
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
                            liveErrorAgencyList.postValue("Error")
                        }

                        is DataState.Success -> {
                            httpRepository.filterResponse(
                                it.data as Response<*>,
                                HttpListener({ success ->
                                    try {
                                        val data = Gson().fromJson(
                                            success.toString(),
                                            AgencyRes::class.java
                                        )

                                        Log.d("oykwon", "Data : $data")

                                        if (data != null) {
                                            liveAgencyList.postValue(data.resultData.agencyInfoList)
                                        }
                                    } catch (e: Exception) {
                                        liveErrorAgencyList.postValue("Error")
                                    }
                                }, { fail ->
                                    try {
                                        val errorData =
                                            Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                        liveErrorAgencyList.postValue("Error")
                                    } catch (e: Exception) {
                                        liveErrorAgencyList.postValue("Error")
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
            if (documentModel != null) {
                httpRepository.getDocument(documentModel).collect {
                    when (it) {
                        is DataState.Loading -> {

                        }

                        is DataState.Error -> {
                            Log.d("oykwon", "Get Docu Error 1")
                            liveErrorDocument.postValue("Error")
                        }

                        is DataState.Success -> {
                            httpRepository.filterResponse(
                                it.data as Response<*>,
                                HttpListener({ success ->
                                    try {
                                        val data = Gson().fromJson(
                                            success.toString(),
                                            DocumentRes::class.java
                                        )
                                        if (data != null) {
                                            liveDocument.postValue(data)
                                        }
                                    } catch (e: Exception) {
                                        Log.d("oykwon", "Get Docu Error 2 " + e.message)
                                        liveErrorDocument.postValue("Error")
                                    }
                                }, { fail ->
                                    try {
                                        val errorData =
                                            Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                        Log.d("oykwon", "Get Docu Error 3 " + errorData.resultMsg)
                                        liveErrorDocument.postValue("Error")
                                    } catch (e: Exception) {
                                        Log.d("oykwon", "Get Docu Error 4 " + e.message)
                                        liveErrorDocument.postValue("Error")
                                    }
                                })
                            )
                        }
                    }
                }
            }
        }
    }

    fun signDocument(signDocumentModel: SignDocumentModel) {
        CoroutineScope(Dispatchers.IO).launch {
            if (signDocumentModel != null) {
                httpRepository.signDocument(signDocumentModel).collect {
                    when (it) {
                        is DataState.Loading -> {

                        }

                        is DataState.Error -> {
                            liveErrorDocument.postValue("Error")
                        }

                        is DataState.Success -> {
                            httpRepository.filterResponse(
                                it.data as Response<*>,
                                HttpListener({ success ->
                                    try {
                                        val data = Gson().fromJson(
                                            success.toString(),
                                            SignDocumentRes::class.java
                                        )
                                        if (data != null) {
                                            liveSignDocument.postValue(data)
                                        }
                                    } catch (e: Exception) {
                                        liveErrorDocument.postValue("Error")
                                    }
                                }, { fail ->
                                    try {
                                        val errorData =
                                            Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                        liveErrorDocument.postValue("Error")
                                    } catch (e: Exception) {
                                        liveErrorDocument.postValue("Error")
                                    }
                                })
                            )
                        }
                    }
                }
            }
        }
    }

    fun checkDocumentStatus(checkDocumentModel: CheckDocumentModel) {
        CoroutineScope(Dispatchers.IO).launch {
            if (checkDocumentModel != null) {
                httpRepository.checkDocumentStatus(checkDocumentModel).collect {
                    when (it) {
                        is DataState.Loading -> {

                        }

                        is DataState.Error -> {
                            liveErrorDocument.postValue("Error")
                        }

                        is DataState.Success -> {
                            httpRepository.filterResponse(
                                it.data as Response<*>,
                                HttpListener({ success ->
                                    try {
                                        val data = Gson().fromJson(
                                            success.toString(),
                                            SignDocumentStatusRes::class.java
                                        )

                                        Log.d("oykwon", "success : " + success)
                                        Log.d("oykwon", "success : " + data)

                                        if (data != null) {
                                            liveSignDocumentStatus.postValue(data)
                                            // Complete 서버 측 완료 후 테스트 해야함.
//                                            liveSignDocument.postValue(data)
                                        }
                                    } catch (e: Exception) {
                                        liveErrorDocument.postValue("Error")
                                    }
                                }, { fail ->
                                    try {
                                        val errorData =
                                            Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                        liveErrorDocument.postValue("Error")
                                    } catch (e: Exception) {
                                        liveErrorDocument.postValue("Error")
                                    }
                                })
                            )
                        }
                    }
                }
            }
        }
    }
}