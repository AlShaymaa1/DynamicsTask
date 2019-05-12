package com.dynamicsapp.usecaseLayer.domain.network;

import com.dynamicsapp.entitiesLayer.placesModels.PlacedResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ApiInterface {

    @GET("maps/api/place/autocomplete/json?types=geocode&location=37.76999,-122.44696&radius=500&key=AIzaSyBsNP6FqRsVC26H8Y9Atxd2_7bu_435XIM")
    Observable <PlacedResponse> getOneItemDetails(@Query("input") String input);

}
