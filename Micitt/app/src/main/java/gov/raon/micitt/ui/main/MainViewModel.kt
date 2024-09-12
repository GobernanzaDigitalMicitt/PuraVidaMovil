package gov.raon.micitt.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import gov.raon.micitt.di.DataState
import gov.raon.micitt.di.local.LocalRepository
import gov.raon.micitt.di.repository.HttpRepository
import gov.raon.micitt.di.repository.http.HttpListener
import gov.raon.micitt.models.CheckAuthModel
import gov.raon.micitt.models.SignModel
import gov.raon.micitt.models.response.CheckSignInStatusRes
import gov.raon.micitt.models.response.CheckSignUpStatusRes
import gov.raon.micitt.models.response.ErrorRes
import gov.raon.micitt.models.response.SignRes
import gov.raon.micitt.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val httpRepository: HttpRepository
) : ViewModel() {

    val liveSignUpResponse = MutableLiveData<SignRes>()
    val liveSignInResponse = MutableLiveData<SignRes>()
    val liveSignErrorResponse = MutableLiveData<String>()

    val liveCheckSignUpStatus = MutableLiveData<CheckSignUpStatusRes>()
    val liveCheckSignInStatus = MutableLiveData<CheckSignInStatusRes>()
    val liveCheckAuthErrorResponse = MutableLiveData<String>()

    fun reqSignUp(context: Context, signModel: SignModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.signUp(signModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data =
                                        Gson().fromJson(success.toString(), SignRes::class.java)
                                    if (data != null) {
                                        liveSignUpResponse.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    liveSignErrorResponse.postValue("SignUp Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData =
                                        Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    liveSignErrorResponse.postValue("SignUp Request Server Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    liveSignErrorResponse.postValue("SignUp Request Error : " + e.message)
                                }
                            })
                        )
                    }

                    else -> {
                        liveSignErrorResponse.postValue("Request Network Error")
                    }
                }
            }
        }
    }

    fun reqSignIn(context: Context, signModel: SignModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.signIn(signModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data =
                                        Gson().fromJson(success.toString(), SignRes::class.java)
                                    if (data != null) {
                                        liveSignInResponse.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    liveSignErrorResponse.postValue("SignIn Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData =
                                        Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    liveSignErrorResponse.postValue("SignIn Request Server Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    liveSignErrorResponse.postValue("SignIn Request Error : " + e.message)
                                }
                            })
                        )
                    }

                    else -> {
                        liveSignErrorResponse.postValue("Request Network Error")
                    }
                }
            }
        }
    }

    fun reqCheckSignUpStatus(checkAuthModel: CheckAuthModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.checkAuthSignUp(checkAuthModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data = Gson().fromJson(
                                        success.toString(),
                                        CheckSignUpStatusRes::class.java
                                    )
                                    if (data != null) {
                                        liveCheckSignUpStatus.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    liveCheckAuthErrorResponse.postValue("SignUp Status Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData =
                                        Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    liveCheckAuthErrorResponse.postValue("SignUp Status Server Request Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    liveCheckAuthErrorResponse.postValue("SignUp Status Request Error : " + e.message)
                                }
                            })
                        )
                    }

                    else -> {
                        liveSignErrorResponse.postValue("Request Network Error")
                    }
                }
            }
        }
    }

    fun reqSignInStatus(checkAuthModel: CheckAuthModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.checkAuthSignIn(checkAuthModel).collect {
                when (it) {
                    is DataState.Loading -> {

                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data = Gson().fromJson(
                                        success.toString(),
                                        CheckSignInStatusRes::class.java)

                                    if (data != null) {
                                        liveCheckSignInStatus.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    liveCheckAuthErrorResponse.postValue("SignIn Status Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData =
                                        Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    liveCheckAuthErrorResponse.postValue("SignIn Status Server Request Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    liveCheckAuthErrorResponse.postValue("SignIn Status Request Error : " + e.message)
                                }
                            })
                        )
                    }

                    else -> {
                        liveSignErrorResponse.postValue("Request Network Error")
                    }
                }
            }
        }
    }
}