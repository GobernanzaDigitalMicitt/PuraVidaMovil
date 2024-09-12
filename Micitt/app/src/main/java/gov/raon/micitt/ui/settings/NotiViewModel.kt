package gov.raon.micitt.ui.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import gov.raon.micitt.di.DataState
import gov.raon.micitt.models.response.NotificationRes
import gov.raon.micitt.di.repository.HttpRepository
import gov.raon.micitt.di.repository.http.HttpListener
import gov.raon.micitt.models.NotificationModel
import gov.raon.micitt.models.response.ErrorRes
import gov.raon.micitt.models.response.SignOutRes
import gov.raon.micitt.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NotiViewModel @Inject constructor(
    private val httpRepository: HttpRepository
) : ViewModel() {
    val liveList = MutableLiveData<NotificationRes>()
    val liveListError = MutableLiveData<String>()

    fun <T> getNotice(context: Context, notification: NotificationModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.getNotice(notification).collect {
                when (it) {
                    DataState.Loading -> {

                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success ->
                                try {
                                    val data = Gson().fromJson(
                                        success.toString(),
                                        NotificationRes::class.java
                                    )
                                    if (data != null) {
                                        if (data.resultData.notificationList.isEmpty()) {

                                        }
                                        liveList.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    liveListError.postValue("Notification Error : " + e.message)
                                    e.printStackTrace()
                                }
                            }, { fail ->
                                try {
                                    val data = Gson().fromJson(
                                        fail.toString(),
                                        NotificationRes::class.java
                                    )
                                    liveListError.postValue("Notification Server Request Error : " + data.resultMsg)
                                } catch (e: Exception) {
                                    liveListError.postValue("Notification Request Error")
                                    e.printStackTrace()
                                }
                            })
                        )
                    }

                    is DataState.Error -> {
                        liveListError.postValue("Request Network Error")
                    }
                }
            }
        }
    }

    fun <T> withdraw(context: Context, hashedToken : String){
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.withdraw(hashedToken).collect{
                when(it){
                    DataState.Loading ->{
                    }
                    is DataState.Success ->{
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success -> try{
                                val data = Gson().fromJson(success.toString(), SignOutRes::class.java)
                                if(data != null){

                                }
                            } catch (e: Exception){
                                e.printStackTrace()
                            }

                            },{fail ->
                                try{
                                    val error = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                } catch (e: Exception){
                                    e.printStackTrace()
                                }
                            })
                        )
                    }
                    is DataState.Error -> {

                    }
                }
            }
        }
    }

    fun <T> logOut(context: Context, hashedToken : String){
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.logOut(hashedToken).collect{
                when(it){
                    DataState.Loading ->{
                    }
                    is DataState.Success ->{
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success -> try{
                                val data = Gson().fromJson(success.toString(), SignOutRes::class.java)
                                if(data != null){
                                }
                            } catch (e: Exception){
                                e.printStackTrace()
                            }

                            },{fail ->
                                try{
                                    val error = Gson().fromJson(fail.toString(), ErrorRes::class.java)
                                } catch (e: Exception){
                                    e.printStackTrace()
                                }
                            })
                        )
                    }
                    is DataState.Error -> {

                    }
                }
            }
        }
    }
}