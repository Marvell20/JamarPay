package com.example.jamarpay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServiceJava {
    @GET("/credito/payoro/result-validate_identity/{company}/{registro_id}")
    Call<ResponseValidateIdentityJava> resultValidateIdentity(@Path("company") String company, @Path("registro_id") String registro_id);
}
