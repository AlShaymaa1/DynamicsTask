package com.dynamicsapp.appLayer.features.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.dynamicsapp.entitiesLayer.placesModels.PlacedResponse;
import com.dynamicsapp.usecaseLayer.domain.usecases.SearchCountryByNameUseCase;

public class SearchCountryByNameViewModel extends ViewModel {

    private SearchCountryByNameUseCase useCase;
    public MutableLiveData<PlacedResponse> result;

    public SearchCountryByNameViewModel() {
        result=new MutableLiveData<>();
        useCase=new SearchCountryByNameUseCase(result);
    }


    public void retrievePlaces(String input){
       useCase.getCountries(input);
    }
}
