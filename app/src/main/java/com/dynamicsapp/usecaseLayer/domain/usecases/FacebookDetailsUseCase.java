package com.dynamicsapp.usecaseLayer.domain.usecases;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dynamicsapp.appLayer.core.MyApplication;
import com.dynamicsapp.usecaseLayer.domain.SavedInstance;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class FacebookDetailsUseCase {


    private MutableLiveData<String> fullNameLiveData;
    private MutableLiveData<String> userIdLiveData;
    private static final String USER_ID = "userId";
    private static final String FULL_NAME = "fullName";
    private static final String PREF_NAME = "shared_pref";

    public FacebookDetailsUseCase(
            MutableLiveData<String> userIdLiveData
            , MutableLiveData<String> fullNameLiveData) {
        this.fullNameLiveData = fullNameLiveData;
        this.userIdLiveData = userIdLiveData;
    }

    private void loginWithFacebook(CallbackManager callbackManager) {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String userId = loginResult.getAccessToken().getUserId();
                        String token = loginResult.getAccessToken().getToken();
                        Log.e("success", userId + token);
                        setFacebookData(loginResult);

                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
    }

    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.e("Response", response.toString());
                            String firstName = response.getJSONObject().getString("first_name");
                            Log.e("Login" + "FirstName", firstName);
                            String lastName = response.getJSONObject().getString("last_name");
                            String userId = response.getJSONObject().getString("id");
                            Log.e("Login" + "LastName", lastName);
                            String fullName = firstName + " " + lastName;
                            fullNameLiveData.postValue(fullName);
                            saveDataInSharedPref(fullName, userId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void saveDataInSharedPref(String fullName, String id) {
        SharedPreferences.Editor editor = MyApplication.getAppContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(FULL_NAME, fullName);
        editor.putString(USER_ID, id);
        editor.apply();
    }

    public void getFullNameFromShared() {
        SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (prefs.contains(FULL_NAME)) {
            String fullName = prefs.getString(FULL_NAME, null);
            if (!TextUtils.isEmpty(fullName)) {
                fullNameLiveData.postValue(fullName);
            }
        }
    }

    public void getUserIdFromShared() {
        SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (prefs.contains(USER_ID)) {
            String userId = prefs.getString(USER_ID, null);
            if (!TextUtils.isEmpty(userId)) {
                userIdLiveData.postValue(userId);
            }
        }

    }

    private boolean isLoggedIn() {
        return  AccessToken.getCurrentAccessToken() != null;
    }

    private void logOut(){
        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    if(!SavedInstance.getInstance().isChecked()){
                        Log.d("FB", "User Logged Out.");
                        fullNameLiveData.postValue("");
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        };
        tokenTracker.startTracking();
    }

    public void checkFacebookState(CallbackManager callbackManager){
        if(isLoggedIn()){
           logOut();
        }else {
          loginWithFacebook(callbackManager);
        }
    }
}
