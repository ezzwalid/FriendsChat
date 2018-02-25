package com.example.prins.friendschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.listeners.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by prins on 2/25/2018.
 */

public class UsersMultiSelectAdapter extends RecyclerView.Adapter<UsersMultiSelectAdapter.UserViewHolder>{

    ArrayList<User> users;

    MultiSelector multiSelector;


    public UsersMultiSelectAdapter(ArrayList<User> users, MultiSelector multiSelector) {
        this.users = users;
        this.multiSelector = multiSelector;
        multiSelector.setSelectable(true);
    }

    @Override
    public UsersMultiSelectAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsersMultiSelectAdapter.UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_chat_list, null));
    }

    @Override
    public void onBindViewHolder(UsersMultiSelectAdapter.UserViewHolder holder, int position) {
        holder.bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends SwappingHolder implements View.OnClickListener{
        @BindView(R.id.user_image)
        CircleImageView imageView;
        @BindView(R.id.user_name)
        TextView userNameTv;
        public UserViewHolder(View itemView) {
            super(itemView, multiSelector);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final User user){
            if (user.getImage_url() != null){
                Picasso.with(imageView.getContext()).load(user.getImage_url()).into(imageView);
            }
            else {
                imageView.setImageResource(R.mipmap.frinds_logo);
            }
            userNameTv.setText(user.getName());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemView.isActivated()){
                multiSelector.setSelected(UserViewHolder.this, false);
            }
            else {
                multiSelector.setSelected(UserViewHolder.this, true);
            }
        }
    }

    public void addUser(User user){
        this.users.add(user);
        notifyDataSetChanged();
    }

    public void replaceUsers(ArrayList<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    public void addUsers(ArrayList<User> users){
        this.users.addAll(users);
        notifyDataSetChanged();
    }

}
