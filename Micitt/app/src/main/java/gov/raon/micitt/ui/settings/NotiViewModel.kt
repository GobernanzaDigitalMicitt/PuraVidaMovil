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

    fun <T> getNotice(context: Context, notification: NotificationModel) {
        CoroutineScope(Dispatchers.IO).launch {
            httpRepository.getNotice(notification).collect {
                when (it) {
                    DataState.Loading -> {

                    }

                    is DataState.Success -> {
                        httpRepository.filterResponse(
                            it.data as Response<*>,
                            HttpListener({ success -> try {
                                    val data = Gson().fromJson(success.toString(), NotificationRes::class.java)
                                    if (data != null) {
                                        liveList.postValue(data)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }, { fail ->
                                try {
                                    val data = Gson().fromJson(fail.toString(), NotificationRes::class.java)
                                } catch (e: Exception) {
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