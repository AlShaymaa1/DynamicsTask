package com.dynamicsapp.usecaseLayer.domain.usecases;


import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.dynamicsapp.entitiesLayer.placesModels.PlacedResponse;
import com.dynamicsapp.usecaseLayer.domain.repository.CountriesRepository;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchCountryByNameUseCase {

    private CountriesRepository repository;
    private MutableLiveData<PlacedResponse> result;

    public SearchCountryByNameUseCase(
    MutableLiveData<PlacedResponse> result) {
        repository=new CountriesRepository();
        this.result=result;

    }

    public void getCountries(String input){
        Observable<PlacedResponse> call= repository.getCountries(input);
        call.subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<PlacedResponse>() {
                     @Override
                     public void onSubscribe(Disposable d) {
                     }
                     @Override
                     public void onNext(PlacedResponse places) {
                         Log.e("success",places.getPredictions().size()+"");
                         result.postValue(places);
                     }
                     @Override
                     public void onError(Throwable e) {
                         Log.e("error",e.getMessage());
                     }

                     @Override
                     public void onComplete() {
                     }
                 });
    }

}
