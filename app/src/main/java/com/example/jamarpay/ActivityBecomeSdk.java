package com.example.jamarpay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeCallBackManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeInterfaseCallback;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeResponseManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.LoginError;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityBecomeSdk extends AppCompatActivity {

    private final BecomeCallBackManager mCallbackManager = BecomeCallBackManager.createNew ( );

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_sdk);

        String validatiopnTypes =  "VIDEO/PASSPORT/DNI/LICENSE" ;
        String clientSecret =  "AK5OZ59W2EV61GSM0FQXNBRU4DTJH3PI" ;
        String clientId = "jamarprod";
        String contractId = "72";

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userid");

        Log.i("userid2", intent.getStringExtra("userid"));

        BecomeResponseManager.getInstance().startAutentication(ActivityBecomeSdk.this,
                new BDIVConfig(clientId,
                        clientSecret,
                        contractId,
                        validatiopnTypes,
                        true,
                        null,
                        userId
                )
        );

        BecomeResponseManager.getInstance().registerCallback(mCallbackManager, new BecomeInterfaseCallback() {

            @Override
            public void onSuccess(ResponseIV responseIV) {

                //validating identity
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://dev.appsjamar.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiServiceJava apiService = retrofit.create(ApiServiceJava.class);

                Call<ResponseValidateIdentityJava> call = apiService.resultValidateIdentity("JA",userId);

                call.enqueue(new Callback<ResponseValidateIdentityJava>() {
                    @Override
                    public void onResponse(Call<ResponseValidateIdentityJava> call, Response<ResponseValidateIdentityJava> response) {
                        if(response.isSuccessful() && response.body().isSuccess()){
                            ResponseValidateIdentityJava responseValidateIdentityJava = response.body();
                            Intent intent = new Intent(ActivityBecomeSdk.this, common.ConfirmedIdentity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i("respuesta","algo falló");
                            Log.i("respuesta2",response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseValidateIdentityJava> call, Throwable t) {
                        Log.i("respuesta","algo falló, entró a onFailure");
                    }

                });

            }

            @Override
            public void onCancel() {
                Log.i("cancel", "cancel by user");
            }

            @Override
            public void onError(LoginError loginError) {
                Log.d("Error", loginError.getMessage());
            }
        });

    }
}