package com.example.prins.friendschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.listeners.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by prins on 2/23/2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    ArrayList<User> users;

    RecyclerItemClickListener<User> clickListener;

    public UsersAdapter(ArrayList<User> users, RecyclerItemClickListener<User> clickListener) {
        this.users = users;
        this.clickListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_chat_list, null));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_image)
        CircleImageView imageView;
        @BindView(R.id.user_name)
        TextView userNameTv;
        public UserViewHolder(View itemView) {
            super(itemView);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(user);
                }
            });
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
