package com.zoolife.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mukesh.OtpView;
import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.SignInResponse.SignInResponseModel;
import com.zoolife.app.ResponseModel.SignupResponseModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerification extends AppBaseActivity implements View.OnClickListener {
    private OtpView otpView;
    String otp1 = "";
    String phone = "";
    String userName = "";
    String from = "";
    String password = "";
    String type = "";
    private TextView otpTV;
    ProgressBar progress_circular;
    ProgressBar progressLiner;
    RelativeLayout signupButton;
    private TextView resend_otp;
    private TextView tv_phone;
    CountDownTimer cTimer = null;
    private static final String TAG = "OTPVerification";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static MutableLiveData<Boolean> successListener = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);
        mAuth = FirebaseAuth.getInstance();
        initializeUi();
        setListeners();
        type = getIntent().getStringExtra("type");
        phone = getIntent().getStringExtra("phone");
        tv_phone.setText(phone);
        if (type.trim().equals("1")) {
            userName = getIntent().getStringExtra("userName");
            password = getIntent().getStringExtra("password");
        }

        callBacks();
        startPhoneNumberVerification(phone);

    }

    void callBacks() {
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                progressLiner.setVisibility(View.GONE);
                // Save verification ID and resending token so we can use them later
                Toast.makeText(OTPVerification.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
                startTimer();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLightStatusBar();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signupButton) {
            String str = otpView.getText().toString();
            if (mVerificationId != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, str);
                signInWithPhoneAuthCredential(credential);
            }
        }
    }


    private void initializeUi() {
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
        otpView = findViewById(R.id.otp_view);
        otpTV = findViewById(R.id.otpTV);
        progressLiner = findViewById(R.id.progressBar);
        progress_circular = findViewById(R.id.progress_circular);
        resend_otp = findViewById(R.id.resend_otp);
        tv_phone = findViewById(R.id.tv_phone);
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resend_otp.getText().equals(getString(R.string.resend_code))) {
                    if (mResendToken != null) {
                        resendVerificationCode(phone, mResendToken);
                    } else {
                        startPhoneNumberVerification(phone);
                    }
                }
            }
        });
    }

    private void setListeners() {

        otpView.setOtpCompletionListener(otp -> {
//            mAuth.applyActionCode(otp);
            if (mVerificationId != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished / 1000) > 9) {
                    resend_otp.setText(String.format(getString(R.string.resend_code_after), +(millisUntilFinished / 1000)));
                } else {
                    resend_otp.setText(String.format(getString(R.string.resend_code_after), (millisUntilFinished / 1000)));
                }

            }

            public void onFinish() {
                resend_otp.setText(getString(R.string.resend_code));

            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            if (type.trim().equals("1")) {
                                register();
                            } else {
                                finishAffinity();
                                Intent intent = new Intent(OTPVerification.this, ChangePassword.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });


    }

    public void signInUser() {

        progress_circular.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<JsonObject> call = apiService.signIn(phone, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.isSuccessful() && response.body().getAsJsonObject().get("data").isJsonObject()
                        && !response.body().getAsJsonObject().get("error").getAsBoolean()) {

                    Gson gson = new Gson();
                    SignInResponseModel responseModel = gson.fromJson(response.body().getAsJsonObject().toString(), SignInResponseModel.class);

//                    SignInResponseModel responseModel =   new Gson().toJson(response.body());
//                    SignInResponseModel responseModel = response.body().getAsJsonObject();

                    Log.d(TAG, response.toString());
                    session.setSurName(responseModel.getData().getSurname());
                    session.setFullName(responseModel.getData().getFullname());
                    session.setUserId(responseModel.getData().getId());
                    session.setEmail(responseModel.getData().getEmail());
                    session.setPhone(responseModel.getData().getPhone());
                    session.setLanguage(responseModel.getData().getLanguage());
                    session.setCountryId(responseModel.getData().getCountryId());
                    session.setCountry(responseModel.getData().getCountry());
                    session.setCity(responseModel.getData().getCity());
                    session.setCityId(responseModel.getData().getCityId());
                    session.setYear(responseModel.getData().getBYear());
                    session.setMonth(responseModel.getData().getBMonth());
                    session.setDay(responseModel.getData().getBDay());


                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    preferences.edit().putBoolean("firstTimeAfterLogin", true);

                    finishAffinity();
                    Intent intent = new Intent(OTPVerification.this, MainActivity.class);
                    startActivity(intent);


                    session.setIsLogin(true);
                    // infoDialog("تهانينا ! لقد قمت بتسجيل الدخول بنجاح");
                    progress_circular.setVisibility(View.GONE);

                } else {
                    Log.d(TAG, "Server Error.");
                    progress_circular.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), response.body().getAsJsonObject().get("message").toString(), Toast.LENGTH_LONG).show();
                    //infoDialog("Server Error.");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);

            }
        });

    }

    void register() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        //Call<JsonObject> call=apiService.signUp("register",fullname,email,contactNumber,password);
        Call<JsonObject> call = apiService.signUp(userName, phone, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                SignupResponseModel responseModel = response.body();
                if (response.isSuccessful() &&
                        !response.body().getAsJsonObject().get("error").getAsBoolean()) {

                    Gson gson = new Gson();
                    SignupResponseModel responseModel = gson.fromJson(response.body().getAsJsonObject().toString(), SignupResponseModel.class);

                    Log.d(TAG, response.toString());
                    signInUser();

                    progress_circular.setVisibility(View.GONE);
                } else {
                    // infoDialog("Server Error.");
                    progress_circular.setVisibility(View.GONE);
                    Log.d("sdhjksd", response.body().toString());
                   showDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Log.d(TAG, strr);
//                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });

    }


    public void showDialog() {
        final Dialog dialog = new Dialog(OTPVerification.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialoge);


        TextView dialogButton = dialog.findViewById(R.id.btnCancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}