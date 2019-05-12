package com.dynamicsapp.usecaseLayer.domain.repository;

import com.dynamicsapp.appLayer.core.MyApplication;
import com.dynamicsapp.entitiesLayer.placesModels.PlacedResponse;
import com.dynamicsapp.usecaseLayer.domain.network.ApiClientRx;
import com.dynamicsapp.usecaseLayer.domain.network.ApiInterface;

import java.util.List;

import io.reactivex.Observable;

public class CountriesRepository {

    private ApiInterface apiInterface;

    public CountriesRepository() {
      apiInterface= ApiClientRx.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
    }

    public Observable<PlacedResponse> getCountries(String input){
       return apiInterface.getOneItemDetails(input);
    }
}
