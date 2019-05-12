package com.dynamicsapp.appLayer.features.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.dynamicsapp.R;
import com.dynamicsapp.appLayer.features.map.MapScreen;
import com.dynamicsapp.entitiesLayer.placesModels.PlacedResponse;

import java.util.ArrayList;
import java.util.List;


public class SearchScreen extends AppCompatActivity {

    private ListView countryNameListView;
    private List<String> countriesList;
    private ArrayAdapter arrayAdapter;
    private EditText countryNameEditText;
    private ImageView searchImageView;
    private Button openMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        initViews();
        SearchCountryByNameViewModel viewModel = ViewModelProviders.of(this).get(SearchCountryByNameViewModel.class);
        searchImageView.setOnClickListener(view -> {
            String countryName = countryNameEditText.getText().toString();
            viewModel.retrievePlaces(countryName);
        });
        openMapButton.setOnClickListener(view -> {
            Intent intent = new Intent(SearchScreen.this, MapScreen.class);
            startActivity(intent);
        });
        viewModel.result.observe(this, result -> {
            getPlaces(result);
            arrayAdapter = new ArrayAdapter<String>(
                    SearchScreen.this,
                    R.layout.contacts_list_item,
                    R.id.contact_text_view, countriesList
            );
            countryNameListView.setAdapter(arrayAdapter);
        });

    }

    private void initViews() {
        countryNameListView = findViewById(R.id.country_name_list_view);
        countriesList = new ArrayList<String>();
        countryNameEditText = findViewById(R.id.country_name_edit_text);
        searchImageView = findViewById(R.id.search_image_view);
        openMapButton = findViewById(R.id.open_map_button);
    }

    private void getPlaces(PlacedResponse response) {
        countriesList.clear();
        int size = response.getPredictions().size();
        for (int i = 0; i < size; i++) {
            countriesList.add(response.getPredictions().get(i).getTerms().get(0).getValue());
        }
    }
}

