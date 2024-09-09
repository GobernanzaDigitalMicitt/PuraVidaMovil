package gov.raon.micitt.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import gov.raon.micitt.di.DataState
import gov.raon.micitt.di.local.LocalRepository
import gov.raon.micitt.di.repository.HttpRepository
import gov.raon.micitt.di.repository.http.HttpListener
import gov.raon.micitt.models.AgencyModel
import gov.raon.micitt.models.CheckDocumentModel
import gov.raon.micitt.models.DocumentModel
import gov.raon.micitt.models.SignDocumentModel
import gov.raon.micitt.models.response.AgencyInfo
import gov.raon.micitt.models.response.AgencyRes
import gov.raon.micitt.models.response.CheckDocumentStatusRes
import gov.raon.micitt.models.response.DocumentRes
import gov.raon.micitt.models.response.ErrorRes
import gov.raon.micitt.models.response.SignDocumentRes
import gov.raon.micitt.utils.Log
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
    val liveCheckDocumentStatus = MutableLiveData<SignDocumentRes>()
    val liveErrorDocument = MutableLiveData<String>()

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
                                            CheckDocumentStatusRes::class.java
                                        )
                                        if (data != null) {
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