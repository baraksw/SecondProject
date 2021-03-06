package com.example.secondproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder> {
    private ArrayList<User> my_friends;
    private Context context;

    public FriendRecyclerAdapter( ArrayList<User> my_friends){
        this.my_friends = my_friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row_layout, parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        context = parent.getContext();
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

        String friend_name = my_friends.get(position).getFull_name();
        int friend_xp = my_friends.get(position).getXp_cnt();

        holder.friend_name.setText(my_friends.get(position).getFull_name());
        holder.friend_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendProfileIntent = new Intent(context, FriendProfileActivity.class);
                friendProfileIntent.putExtra("friend_name", friend_name);
                friendProfileIntent.putExtra("friend_xp", friend_xp);
                context.startActivity(friendProfileIntent);
            }
        });

        setFriendXPText(holder, my_friends.get(position));
    }

    private void setFriendXPText(FriendViewHolder holder, User user) {
        DatabaseReference db_Reference = FirebaseDatabase.getInstance().getReference();
        db_Reference.child("DB").child("users_db").child(user.getFull_name()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.friend_xp.setText(String.valueOf(dataSnapshot.child("xp_cnt").getValue(Integer.class)) + " XP");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Set friend xp", "setting friend's xp canceled");
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_friends.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView friend_name;
        TextView friend_xp;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friend_name = itemView.findViewById(R.id.friend_row_full_name);
            friend_xp = itemView.findViewById(R.id.friend_row_xp_textView);
        }
    }

}