package com.example.jamarpay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeCallBackManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeInterfaseCallback;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeResponseManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.LoginError;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

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
        String userId = "64564240";

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
                Log.i("Become","Login Exitoso");
                /*Intent intent = new Intent(ActivityBecomeSdk.this, common.ConfirmedIdentity.class);
                intent.putExtra("responseIV", (Parcelable) responseIV);
                intent.putExtra("idUser",  userId);
                startActivity(intent);
                finish();*/
            }

            @Override
            public void onCancel() {
                Log.d("cancel", "cancel by user");
            }

            @Override
            public void onError(LoginError loginError) {
                Log.d("Error", loginError.getMessage());
            }
        });

    }
}