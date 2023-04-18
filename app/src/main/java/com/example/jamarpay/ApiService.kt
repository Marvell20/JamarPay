package com.example.jamarpay

import retrofit2.Call
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

}