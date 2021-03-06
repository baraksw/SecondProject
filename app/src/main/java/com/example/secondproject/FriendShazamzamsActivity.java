package com.example.secondproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendShazamzamsActivity extends AppCompatActivity {

    private static final String TAG = "FriendShazamzamsActivit";
    private String current_friend_name;
    private int current_friend_xp;
    private GridLayoutManager friend_hums_LayoutManager;
    private RecyclerView friend_hums_RecyclerView;
    private ArrayList<Hum> temp_friend_hums;
    private DatabaseReference hums_db_Reference;
    private HumRecyclerAdapter hum_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_shazamzams);

        Log.d(TAG, "onCreate: started.");

        Intent temp_intent = getIntent();
        current_friend_name = temp_intent.getStringExtra("friend_name");
        current_friend_xp = temp_intent.getIntExtra("friend_xp", -1);
        Log.d(TAG, "onCreate: friend name:" + current_friend_name);
        Log.d(TAG, "onCreate: friend xp:" + current_friend_xp);

        friend_hums_LayoutManager = new GridLayoutManager(this, 1);
        friend_hums_RecyclerView = findViewById(R.id.friends_shazamzams_recyclerView);
        friend_hums_RecyclerView.setHasFixedSize(true);
        friend_hums_RecyclerView.setLayoutManager(friend_hums_LayoutManager);

        temp_friend_hums = new ArrayList<Hum>();

        hums_db_Reference = FirebaseDatabase.getInstance().getReference().child("db2").child("hums_db");
        hums_db_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp_friend_hums.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Hum hum = dataSnapshot1.getValue(Hum.class);
                    if(hum.owner.equals(current_friend_name) == true){
                        hum.hum_answer = dataSnapshot1.child("hum_answer").getValue(String.class);
                        temp_friend_hums.add(hum);
                    }
                }
                hum_adapter = new HumRecyclerAdapter(FriendShazamzamsActivity.this, temp_friend_hums);
                friend_hums_RecyclerView.setAdapter(hum_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendShazamzamsActivity.this, R.string.general_fail_message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void launchFriendProfileActivity(View view) {
        Intent friendProfileIntent = new Intent(this, FriendProfileActivity.class);
        friendProfileIntent.putExtra("friend_name", current_friend_name);
        friendProfileIntent.putExtra("friend_xp", current_friend_xp);

        Log.d(TAG, "launchFriendProfileActivity: friend name:" + current_friend_name);
        Log.d(TAG, "launchFriendProfileActivity: friend xp:" + current_friend_xp);

        startActivity(friendProfileIntent);
    }
}