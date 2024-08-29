package gov.raon.micitt.di.network

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/api/v1/user/signup")
    suspend fun signUp(@Body body: JsonElement?): Response<JsonObject>

    @POST("/api/v1/notification/get")
    suspend fun getNotice(@Query("pageNo") pageNo: Int, @Query("pageCnt") pageCnt: Int):Response<JsonObject>
}