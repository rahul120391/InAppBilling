package assignments.android.zensar.myinappbilling.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.HashMap;

import assignments.android.zensar.myinappbilling.R;
import assignments.android.zensar.myinappbilling.interactors.ILoginView;
import assignments.android.zensar.myinappbilling.interactors.ITransferApiResult;
import assignments.android.zensar.myinappbilling.model.UserDetailResponse;
import assignments.android.zensar.myinappbilling.model.ZenVerseLoginResponse;
import assignments.android.zensar.myinappbilling.network.ApiClass;
import assignments.android.zensar.myinappbilling.utility.Constants;
import assignments.android.zensar.myinappbilling.utility.Utils;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener, ITransferApiResult {

    EditText _usernameText;
    EditText _passwordText;
    Button _loginButton;
    ApiClass mApiClass;
    FrameLayout mFlProgressBar;
    int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE);
        if(prefs.getString(Constants.USER_ID,"")!=null && !prefs.getString(Constants.USER_ID,"").equalsIgnoreCase(""))
        {
           finish();
           Utils.startAcivity(this,InAppProductsActivity.class);
        }
        else{
            init();
            isStoragePermissionGranted();
        }

    }

    @Override
    public void init() {
        _usernameText = findViewById(R.id.input_username);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(this);
        mFlProgressBar = findViewById(R.id.flProgressBar);

    }

    @Override
    public void hideLoading() {
        mFlProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mFlProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            if (_usernameText.getText().toString().trim().isEmpty()) {
                _usernameText.setError(getString(R.string.username_error));
            } else if (_passwordText.getText().toString().trim().isEmpty()) {
                _passwordText.setError(getString(R.string.password_error));
            } else {
                if (Utils.checkConnectivity(LoginActivity.this)) {
                    if (mApiClass == null) {
                        mApiClass = new ApiClass(this);
                    }
                    position = 1;
                    showLoading();
                    HashMap<String, String> body = new HashMap<>();
                    body.put(Constants.J_USERNAME, _usernameText.getText().toString().trim());
                    body.put(Constants.J_PASSWORD, _passwordText.getText().toString().trim());
                    body.put(Constants.REQUESTED_WITH, Constants.REQUESTED_WITH_VALUE);
                    body.put(Constants.MODULE_NAME, Constants.MODULE_NAME_VALUE);
                    mApiClass.login(body);
                    Utils.hideKeyboard(LoginActivity.this);
                } else {
                    Utils.showToast(LoginActivity.this, getString(R.string.network_error));
                }
            }
        }
    }

    @Override
    public void success(Object object) {

        if (object != null) {
            if (position == 1) {
                ZenVerseLoginResponse response = (ZenVerseLoginResponse) object;
                if (response != null) {
                    if (response.getStatus()==0) {
                        if (mApiClass == null) {
                            mApiClass = new ApiClass(this);
                        }
                        position = 2;
                        HashMap<String, String> body = new HashMap<>();
                        body.put(Constants.USER_ID, _usernameText.getText().toString().trim());
                        body.put(Constants.USERNAME, response.getFullName());
                        body.put(Constants.EMAIL, response.getEmailId());
                        body.put(Constants.PROFILE_URL, "");
                        SharedPreferences prefs = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE);
                        prefs.edit().putString(Constants.USER_ID, _usernameText.getText().toString().trim()).commit();
                        mApiClass.saveUserDetails(body);
                    } else if (response.getStatus()==1) {
                        hideLoading();
                        Utils.showToast(LoginActivity.this, response.getStatusMessage());
                    }
                } else {
                    hideLoading();
                    Utils.showToast(LoginActivity.this, Constants.ERROR);
                }

            } else if (position == 2) {
                hideLoading();
                UserDetailResponse response = (UserDetailResponse) object;
                if (response != null) {
                    if (response.getIsSuccess().equalsIgnoreCase("true")) {
                        _usernameText.setText("");
                        _passwordText.setText("");
                        Utils.startAcivity(LoginActivity.this, InAppProductsActivity.class);
                    } else if (response.getIsSuccess().equalsIgnoreCase("false")) {
                        Utils.showToast(LoginActivity.this, response.getMessage());
                    }
                } else {
                    Utils.showToast(LoginActivity.this, Constants.ERROR);
                }
            }

        }
    }

    @Override
    public void failure(String message) {
        hideLoading();
        Utils.showToast(LoginActivity.this, message);
    }

    @Override
    protected void onDestroy() {
        if (mApiClass != null) {
            mApiClass.cancelRequest();
        }
        super.onDestroy();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
