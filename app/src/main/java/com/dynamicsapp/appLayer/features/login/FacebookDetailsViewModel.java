package com.dynamicsapp.appLayer.features.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.dynamicsapp.usecaseLayer.domain.usecases.FacebookDetailsUseCase;
import com.facebook.CallbackManager;

public class FacebookDetailsViewModel extends ViewModel {

    private FacebookDetailsUseCase useCase;
    public MutableLiveData<String> userIdLiveData;
    public MutableLiveData<String> fullNameLiveData;


    public FacebookDetailsViewModel() {
        userIdLiveData =new MutableLiveData<>();
        fullNameLiveData=new MutableLiveData<>();
        useCase=new FacebookDetailsUseCase( userIdLiveData,fullNameLiveData);

    }

      void loginWithFacebookSdk(CallbackManager callbackManager){
        useCase.checkFacebookState(callbackManager);
    }

     public void getDataSavedFromSharedPref(){
        useCase.getFullNameFromShared();
        useCase.getUserIdFromShared();
     }
}
