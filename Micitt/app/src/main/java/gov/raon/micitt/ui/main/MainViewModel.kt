package gov.raon.micitt.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import gov.raon.micitt.di.DataState
import gov.raon.micitt.di.local.LocalRepository
import gov.raon.micitt.di.repository.HttpRepository
import gov.raon.micitt.di.repository.http.HttpListener
import gov.raon.micitt.models.SignUpModel
import gov.raon.micitt.models.response.SignUpRes
import gov.raon.micitt.utils.Util
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



    fun signUp(context: Context, signUpModel: SignUpModel) {
        CoroutineScope(Dispatchers.IO).launch {

            httpRepository.signUp(signUpModel).collect {
                when(it) {
                    is DataState.Loading -> {

                    }
                    is DataState.Success -> {
                        httpRepository.filterResponse(it.data as Response<*>, HttpListener({ success ->
                            try {
                                val data = Gson().fromJson(success.toString(), SignUpRes::class.java)
                                if (data != null) {
                                    Log.d("oykwon", "Data : " + data.resultData.toString())
                                }
                            } catch (e: Exception) {
                            }
                        }, { fail ->
                            try {
                            } catch (e: Exception) {
                            }
                        }))
                    }
                    else -> {

                    }
                }
            }
        }
    }

}