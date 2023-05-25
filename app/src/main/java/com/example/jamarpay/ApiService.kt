package com.example.jamarpay

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    //@GET("/credito/payoro/obtener_texto_tyc/{company}")
    //suspend fun getTermAndCondition(@Path("company") c_emp: String): Response<TermAndConditionsResponse>

    @GET("/dev/api/v1/{company}/tipo-documentos")
    suspend fun getDocumentType(@Path("company") c_emp: String): Response<DocumentTypeResponse>

    //@POST("/credito/payoro/guardar_respuesta_tyc/{company}")
    //fun postSaveAnswerTermAndCondition(@Body request: TermAndConditionRequest, @Path("company") c_emp: String): Call<TermAndConditionAnswerResponse>

    @GET("/credito/payoro/v1/{company}/cliente-oro/{nit}/segmento")
    suspend fun getGoldClientValidation(@Path("company") company: String, @Path("nit") nit: String): Response<ValidationGoldClientResponse>

    @POST("/credito/payoro/aprovisionamiento/{company}")
    suspend fun sendAprovisionamiento(@Path("company") company: String, @Body requestBody: RequestBody): Response<ProvisioningResponse>

    @GET("credito/payoro/validate_workflow_jamarpay/{nit}/{company}")
    suspend fun getNextProcess(@Path("nit") nit: String, @Path("company") company: String): Response<NextProcessItem>
}