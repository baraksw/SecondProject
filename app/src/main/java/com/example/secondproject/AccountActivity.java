package com.example.secondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    String helloName = "Hey ";
    String name = "NULL";
    private FirebaseAuth mAuth;
    private TextView mUserName;
    private Firebase mRef;
    private TextView mValue;
    private User firt_user;
    private String user_path;
    private String db_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        db_path = "https://secondproject-a6fe3.firebaseio.com/Users/";
        mUserName = findViewById(R.id.helloField);
        mUserName.setText(name);

        mAuth=FirebaseAuth.getInstance();

        user_path = mAuth.getCurrentUser().getUid();
        db_path = db_path + user_path + "/USER";
        mRef = new Firebase(db_path);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firt_user = dataSnapshot.getValue(User.class);
                mUserName.setText("Hey " + firt_user.getFull_name());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void GoToHomePage(View view) {
        Intent HomePageIntent = new Intent(AccountActivity.this,HomePageActivity.class);
        startActivity(HomePageIntent);
    }

    public void LogOut(View view){
       mAuth.signOut();
       Intent HomePageIntent = new Intent(AccountActivity.this,HomePageActivity.class);
       startActivity(HomePageIntent);
    }

}
