package com.dynamicsapp.appLayer.features.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dynamicsapp.R;
import com.dynamicsapp.appLayer.features.search.SearchScreen;

import java.util.ArrayList;
import java.util.Calendar;


public class ContactsScreen extends AppCompatActivity {
    ListView listView;
    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    String name, phoneNumber;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_screen);
        Button openSearchButton = findViewById(R.id.open_search_button);
        Button openCalenderButton = findViewById(R.id.open_calender_button);
        openSearchButton.setOnClickListener(view -> {
            Intent intent = new Intent(ContactsScreen.this, SearchScreen.class);
            startActivity(intent);
        });
        openCalenderButton.setOnClickListener(view -> {
            openCalender();
        });
        initListViews();
        enableRuntimePermission();
        getContactsIntoArrayList();
    }

    private void getContactsIntoArrayList() {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(name + " " + ":" + " " + phoneNumber);
        }

        cursor.close();

    }

    private void enableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                ContactsScreen.this,
                Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(ContactsScreen.this, getResources().getString(R.string.allow_contacts_permission), Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ContactsScreen.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, @Nullable String per[], @Nullable int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ContactsScreen.this, getResources().getString(R.string.permission_granted), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ContactsScreen.this, getResources().getString(R.string.permission_cancelled), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void initListViews() {
        listView = findViewById(R.id.contacts_list_view);
        StoreContacts = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(
                ContactsScreen.this,
                R.layout.contacts_list_item,
                R.id.contact_text_view, StoreContacts
        );

        listView.setAdapter(arrayAdapter);
    }

    private void openCalender() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
    }
}
