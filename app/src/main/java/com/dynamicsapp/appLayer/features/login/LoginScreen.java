package com.dynamicsapp.appLayer.features.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dynamicsapp.R;
import com.dynamicsapp.appLayer.features.profileDetails.ProfileScreen;
import com.dynamicsapp.usecaseLayer.domain.SavedInstance;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import com.facebook.FacebookSdk;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private CallbackManager callbackManager;
    private Button showFiendListButton;
    private LoginButton loginButton;
    private TextView textView;
    private FacebookDetailsViewModel viewModel;
    private CheckBox rememberMeCheckBox;
    private SavedInstance setChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        viewModel = ViewModelProviders.of(this).get(FacebookDetailsViewModel.class);
        initViews();
        showFiendListButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile","email"));

        viewModel.fullNameLiveData.observe(this, info -> {
            textView.setText(info);
        });

        rememberMeCheckBox.setOnCheckedChangeListener((view,isChecked)-> {
            if(isChecked){
                 setChecked=SavedInstance.getInstance();
                 setChecked.setChecked(true);
            }else {
                 setChecked.setChecked(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        showFiendListButton = findViewById(R.id.show_friend_list_button);
        loginButton = findViewById(R.id.login_button);
        textView = findViewById(R.id.info);
        rememberMeCheckBox=findViewById(R.id.remember_me_check_box);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_friend_list_button:
                Intent intent = new Intent(this, ProfileScreen.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                viewModel.loginWithFacebookSdk(callbackManager);
                break;
            default:
                break;
        }
    }
}
