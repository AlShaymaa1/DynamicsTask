package com.dynamicsapp.appLayer.features.profileDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dynamicsapp.R;
import com.dynamicsapp.appLayer.features.contacts.ContactsScreen;
import com.dynamicsapp.appLayer.features.login.FacebookDetailsViewModel;
import com.facebook.login.widget.ProfilePictureView;

import java.io.File;
import java.io.FileOutputStream;

public class ProfileScreen extends AppCompatActivity {


    private ProfilePictureView profilePictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        TextView fullNameTextView = findViewById(R.id.user_name_text_view);
         profilePictureView = findViewById(R.id.friendProfilePicture);
        Button openContactsButton = findViewById(R.id.open_contacts_button);
        FacebookDetailsViewModel viewModel = ViewModelProviders.of(this).get(FacebookDetailsViewModel.class);
        viewModel.getDataSavedFromSharedPref();
        viewModel.userIdLiveData.observe(this, profilePictureView::setProfileId);
        viewModel.fullNameLiveData.observe(this, fullNameTextView::setText);
        openContactsButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileScreen.this, ContactsScreen.class);
            startActivity(intent);
        });
        profilePictureView.setOnClickListener(view -> {
            OnClickShare(profilePictureView);
        });

    }


    public void OnClickShare(View view){

        Bitmap bitmap =getBitmapFromView(profilePictureView);
        try {
            File file = new File(this.getExternalCacheDir(),"logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }
}
