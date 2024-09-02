package gov.raon.micitt.di.repository

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gov.raon.micitt.di.DataState
import gov.raon.micitt.di.network.ApiService
import gov.raon.micitt.di.repository.http.HttpListener
import gov.raon.micitt.models.NotificationModel
import gov.raon.micitt.models.SignUpModel
import gov.raon.micitt.utils.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class HttpRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getElement(jsonStr: String): JsonElement {
        return JsonParser().parse(jsonStr)
    }

    fun getElement(hashMap: HashMap<String, Any>): JsonElement {
        val map = hashMap as Map<*, *>
        val body = JSONObject(map).toString()
        return JsonParser().parse(body)
    }

    suspend fun signUp(signUpModel: SignUpModel): Flow<DataState<Response<JsonObject>>> = flow {
        emit(DataState.Loading)
        try {
            val result = apiService.signUp(signUpModel.toJson())

            emit(DataState.Success(result))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }


    fun <T> filterResponse(response: Response<T>, listener: HttpListener<JSONObject>) {
        try {
            val body = response.body()?.toString()?.let { JSONObject(it) }

            when {
                response.isSuccessful && body != null -> {
                    if (body.getString("resultCode") == "000") {
                        listener.success(body)
                    } else {
                        listener.fail(body)
                    }
                }
                else -> listener.fail("Not Data")
            }
        } catch (e: Exception) {
            listener.fail("Error")
        }

    }

    suspend fun getNotice(notification: NotificationModel): Flow<DataState<Response<JsonObject>>> = flow {
            emit(DataState.Loading)
            try {
                val result = apiService.getNotice(notification.toJson())

                emit(DataState.Success(result))

            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

}