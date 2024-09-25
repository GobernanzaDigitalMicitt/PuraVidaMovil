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
                                    val data = Gson().fromJson(success.toString(), SignRes::class.java)
                                    if (data != null) {
                                        liveSignUpResponse.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.d("SignUp Error :: ${e.message}")
                                    liveSignErrorResponse.postValue("SignUp Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.d("SignUp Request Server Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveSignErrorResponse.postValue("SignUp Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    Log.d("SignUp Request Error :: ${e.message}")
                                    liveSignErrorResponse.postValue("SignUp Error : " + e.message)
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
                                    val data = Gson().fromJson(success.toString(), SignRes::class.java)
                                    if (data != null) {
                                        liveSignInResponse.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.d("SignIn Error :: ${e.message}")
                                    liveSignErrorResponse.postValue("SignIn Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.d("SignIn Request Server Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveSignErrorResponse.postValue("SignIn Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    Log.d("SignUp Request Error :: ${e.message}")
                                    liveSignErrorResponse.postValue("SignIn Error : " + e.message)
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
                                    val data = Gson().fromJson(success.toString(), CheckSignUpStatusRes::class.java)
                                    if (data != null) {
                                        liveCheckSignUpStatus.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.d("SignUp Status Error :: ${e.message}")
                                    liveCheckAuthErrorResponse.postValue("SignUp Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.d("SignUp Status Request Server Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveCheckAuthErrorResponse.postValue("SignUp Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    Log.d("SignUp Status Request Error :: ${e.message}")
                                    liveCheckAuthErrorResponse.postValue("SignUp Error : " + e.message)
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
                                    val data = Gson().fromJson(success.toString(), CheckSignInStatusRes::class.java)
                                    if (data != null) {
                                        liveCheckSignInStatus.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    Log.d("SignIn Status Error :: ${e.message}")
                                    liveCheckAuthErrorResponse.postValue("SignIn Error : " + e.message)
                                }
                            }, { fail ->
                                try {
                                    val errorData = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                    Log.d("SignIn Status Server Request Error :: ${errorData.resultCode} , ${errorData.resultMsg}")
                                    liveCheckAuthErrorResponse.postValue("SignIn Error : " + errorData.resultMsg)
                                } catch (e: Exception) {
                                    Log.d("SignIn Status Request Error :: ${e.message}")
                                    liveCheckAuthErrorResponse.postValue("SignIn Error : " + e.message)
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