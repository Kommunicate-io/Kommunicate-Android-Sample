package io.kommunicate.androidsample;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.applozic.mobicomkit.uiwidgets.kommunicate.views.KmToast;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.json.GsonUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import android.os.ResultReceiver;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.KmConversationHelper;
import io.kommunicate.KmException;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.callbacks.KmPrechatCallback;
import io.kommunicate.models.KmPrechatInputModel;
import io.kommunicate.users.KMUser;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLoginHandler;
import io.kommunicate.utils.KmConstants;

public class MainActivity extends AppCompatActivity {

    EditText mUserId, mPassword;
    AppCompatButton loginButton, visitorButton;
    LinearLayout layout;
    boolean exit = false;
    public static final String APP_ID = "eb775c44211eb7719203f5664b27b59f";
    private static final String INVALID_APP_ID = "INVALID_APPLICATIONID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Kommunicate.init(this, APP_ID);

        layout = (LinearLayout) findViewById(R.id.footerSnack);
        mUserId = (EditText) findViewById(R.id.userId_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        loginButton = (AppCompatButton) findViewById(R.id.btn_signup);
        visitorButton = findViewById(R.id.btn_login_as_visitor);

        TextView txtViewPrivacyPolicy = (TextView) findViewById(R.id.txtPrivacyPolicy);
        txtViewPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isPlaceHolderAppId()) {
                        return;
                    }
                    final String mUserIdText = mUserId.getText().toString().trim();
                    String mPasswordText = mPassword.getText().toString().trim();
                    if (TextUtils.isEmpty(mUserIdText) || mUserId.getText().toString().trim().length() == 0) {
                        KmToast.error(getBaseContext(), com.applozic.mobicomkit.uiwidgets.R.string.enter_user_id, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Logging in..");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    initLoginData(mUserId.getText().toString().trim(), mPassword.getText().toString().trim(), progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        visitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaceHolderAppId()) {
                    return;
                }
//                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
//                progressDialog.setTitle("Logging in..");
//                progressDialog.setMessage("Please wait...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                Kommunicate.init(MainActivity.this, APP_ID);
//                Kommunicate.launchConversationWithPreChat(MainActivity.this, progressDialog, new KmCallback() {
//                    @Override
//                    public void onSuccess(Object message) {
//                        finish();
//                        progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Object error) {
//                        progressDialog.dismiss();
//                        createLoginErrorDialog(null, (Exception) error);
//
//                    }
//                });

                List<KmPrechatInputModel> inputModelList = new ArrayList<>();

                KmPrechatInputModel emailField = new KmPrechatInputModel();
                emailField.setType(KmPrechatInputModel.KmInputType.EMAIL);
                emailField.setRequired(true);
                emailField.setPlaceholder("Enter email");
                //emailField.setValidationRegex(EMAIL_VALIDATION_REGEX);
                emailField.setField("Email");
                emailField.setCompositeRequiredField(getString(com.applozic.mobicomkit.uiwidgets.R.string.phoneNumberEt));

                KmPrechatInputModel nameField = new KmPrechatInputModel();
                nameField.setType(KmPrechatInputModel.KmInputType.TEXT);
                nameField.setPlaceholder("Enter Name");
                nameField.setField(getString(com.applozic.mobicomkit.uiwidgets.R.string.nameEt));

                KmPrechatInputModel contactField = new KmPrechatInputModel();
                contactField.setType(KmPrechatInputModel.KmInputType.NUMBER);
                contactField.setPlaceholder("Enter Phone number");
                //contactField.setValidationRegex(PHONE_NUMBER_VALIDATION_REGEX);
                contactField.setField("Phone");

                KmPrechatInputModel dropdownField = new KmPrechatInputModel();
                dropdownField.setOptions(Arrays.asList("Male", "Female"));
                dropdownField.setPlaceholder("Enter your gender");
                dropdownField.setField("Gender");
                dropdownField.setElement("select");

                inputModelList.add(emailField);
                inputModelList.add(nameField);
                inputModelList.add(contactField);
                inputModelList.add(dropdownField);

                Kommunicate.launchPrechatWithResult(MainActivity.this, inputModelList, new KmPrechatCallback< Map < String, String >>() {
                    @Override
                    public void onReceive(Map < String, String > data, Context context, final ResultReceiver finishActivityReceiver) {

                        //data map will receive the "filedName: entered Text" map. You can get the texts and create a KMUser object here. Then call KmConversationBuilder with that user object.


                        Utils.printLog(context, "TestPre", GsonUtils.getJsonFromObject(data, Map.class));

                        KMUser user = new KMUser();

                        if (!TextUtils.isEmpty(data.get("Email"))) {
                            user.setUserId(data.get("Email"));
                            user.setEmail(data.get("Email"));
                        } else if (!TextUtils.isEmpty(data.get("Mobile number"))) {
                            user.setUserId(data.get("Mobile number"));
                            user.setContactNumber(data.get("Mobile number"));
                        }
                        if (!TextUtils.isEmpty(data.get("Password"))) {
                            user.setPassword(data.get("Password"));
                        }
                        Log.e("dropdown", data.toString());
                        new KmConversationBuilder(MainActivity.this)
                                .setKmUser(user)
                                .launchConversation(new KmCallback() {
                                    @Override
                                    public void onSuccess(Object message) {
                                        finishActivityReceiver.send(KmConstants.PRECHAT_RESULT_CODE, null); //To finish the Prechat activity
                                        Log.d("Conversation", "Success : " + message);
                                    }

                                    @Override
                                    public void onFailure(Object error) {
                                        finishActivityReceiver.send(1000, null); //To dismiss the loading progress bar
                                        Log.d("Conversation", "Failure : " + error);
                                    }
                                });

                    }

                    @Override
                    public void onError(String error) {
                        Utils.printLog(MainActivity.this, "TestPre", "Error : " + error);
                    }
                });


            }
        });
    }

    public boolean isPlaceHolderAppId() {
        if (Kommunicate.PLACEHOLDER_APP_ID.equals(APP_ID)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setCancelable(true);
            dialogBuilder.setMessage(Utils.getString(this, R.string.invalid_app_id_error));
            dialogBuilder.show();
            return true;
        }
        return false;
    }

    public String getInvalidAppIdError(RegistrationResponse registrationResponse) {
        if (registrationResponse != null) {
            if (registrationResponse.getMessage() != null && INVALID_APP_ID.equals(registrationResponse.getMessage())) {
                return getString(R.string.invalid_app_id_error);
            } else {
                return registrationResponse.getMessage();
            }
        }
        return "";
    }

    public void createLoginErrorDialog(RegistrationResponse registrationResponse, Exception exception) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(true);
        StringBuilder message = new StringBuilder(getString(R.string.some_error_occured));
        if (registrationResponse != null) {
            if (!TextUtils.isEmpty(getInvalidAppIdError(registrationResponse))) {
                message.append(" : ");
                message.append(getInvalidAppIdError(registrationResponse));
            }
        } else if (exception != null) {
            message.append(" : ");
            message.append(exception.getMessage());
        }

        dialogBuilder.setMessage(message.toString());
        dialogBuilder.show();
    }

    public void showSnackBar(int resId) {
        Snackbar.make(layout, resId,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onBackPressed() {

        if (exit) {
            finish();
        } else {
            KmToast.success(this, R.string.press_back_exit, Toast.LENGTH_SHORT).show();
            exit = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3000);
        }

    }

    public void initLoginData(String userId, String password, final ProgressDialog progressDialog) {

        final KMUser user = new KMUser();
        user.setUserId(userId);
        user.setApplicationId(APP_ID);

        if (!TextUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        Kommunicate.login(MainActivity.this, user, new KMLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                if (KMUser.RoleType.USER_ROLE.getValue().equals(registrationResponse.getRoleType())) {
                    ApplozicClient.getInstance(context).hideActionMessages(true).setMessageMetaData(null);
                } else {
                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("skipBot", "true");
                    ApplozicClient.getInstance(context).hideActionMessages(false).setMessageMetaData(metadata);
                }

                try {
                    KmConversationHelper.openConversation(context, true, null, new KmCallback() {
                        @Override
                        public void onSuccess(Object message) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Object error) {

                        }
                    });
                } catch (KmException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                createLoginErrorDialog(registrationResponse, exception);
            }
        });
    }
}
