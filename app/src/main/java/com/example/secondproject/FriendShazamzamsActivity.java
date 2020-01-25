package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FriendShazamzamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_shazamzams);
    }

    public void launchFriendProfileActivity(View view) {
        Intent intent = new Intent(this, FriendProfileActivity.class);
        startActivity(intent);
    }
}
