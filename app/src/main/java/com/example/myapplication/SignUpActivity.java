package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "Signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText email = findViewById(R.id.userEmail);
//        String emailAdress = email.getText().toString();
        Log.i(TAG, "onCreate: qusai " + email.getText().toString());
        EditText password = findViewById(R.id.userPassword);

//        String passwordUser = password.getText().toString();
        Log.i(TAG, "onCreate: qusaiPassword " + password.getText().toString() );

        try {
            // Add these lines to add the AWSApiPlugin plugins
//            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());

            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }


        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(view -> {
            Amplify.Auth.fetchAuthSession(
                    result -> Log.i("AmplifyQuickstart", result.toString()),
                    error -> Log.e("AmplifyQuickstart", error.toString())
            );
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), email.getText().toString())
                    .build();
            Log.i(TAG, "onCreate: qusai el rasib + khair el 6abil king of aws logi" + email.getText().toString());
            Amplify.Auth.signUp(email.getText().toString(), password.getText().toString(), options,
                    result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                    error -> Log.e("AuthQuickStart", "Sign up failed", error)
            );

//            Amplify.Auth.confirmSignUp(
//                    email.getText().toString(),
//                    "the code you received via email",
//                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
//                    error -> Log.e("AuthQuickstart", error.toString())
//            );

            Intent intent = new Intent(SignUpActivity.this, ConfirmActivity.class);
            startActivity(intent);

        });
        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
        });



    }
}