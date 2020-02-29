package com.example.secondproject;

import android.media.MediaPlayer;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;

public interface HumToDB {
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recorded_Audio.3pg";
    String hum_answer = "";

    //Play a specific Hum
    default void playAudio(Hum hum) {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        final StorageReference filepath = mStorage.child("Audio").child(hum.getOwner()).child(hum.getHum_id());
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    mediaPlayer.setDataSource(uri.toString());
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    //Insert new Hum to firebase
    default void add_hum_to_db(final Hum new_hum) {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HumsMap hums_map = new HumsMap();
                    hums_map = dataSnapshot.child("db2").getValue(HumsMap.class);
                    Hum hummy = new Hum("tomer", "efuef", 3);
                    hums_map.add_hum(new_hum);
                    mDataBase.child("db2").setValue(hums_map);
                } catch (Exception e) {
                    Log.w("exception", "fuck it");
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    //Play the recently recorded Hum
    default void onClick_play_current_record(View view) {
        final MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(mFileName); //Thats a general name given to every record, and is overrided when
            //we start a new one. So by using mFileName, you can hear the current record.
            player.prepare();
        } catch (IOException e) {
            Log.e("PlayCurrent_log", "prepare() failed");
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    default boolean UploadAnswer (String answer, Hum hum) {
        if (hum.getHum_answer() == null) {
            mDataBase.child("db2").child("hums_db").child(hum.getHum_id()).removeValue();
            mDataBase.child("db2").child("hums_db").child(hum.getHum_id()).setValue(hum).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.w("good1", hum.getHum_answer());

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("semek", "semek");
                        }
                    });

            return true;


                        /*
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("great", task.getException().getMessage());
                    } else {
                        Log.d("fuck", task.getException().getMessage());
                    }
                }
            });

                        */

            /*mDataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.w("good2", dataSnapshot.child("db2").child("hums_db").child(hum.getHum_id()).child("hum_answer").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            }); */

        } else {
            return false;
        }
    }
}





