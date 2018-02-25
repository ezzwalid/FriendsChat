package com.example.prins.friendschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prins.friendschat.Dtos.GroupDialog;
import com.example.prins.friendschat.Dtos.User;
import com.example.prins.friendschat.Dtos.UserGroups;
import com.example.prins.friendschat.R;
import com.example.prins.friendschat.listeners.RecyclerItemClickListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by prins on 2/25/2018.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    private ArrayList<UserGroups> userGroups;

    private RecyclerItemClickListener<UserGroups> clickListener;

    public GroupsAdapter(ArrayList<UserGroups> groupDialogs, RecyclerItemClickListener<UserGroups> clickListener) {
        this.userGroups = groupDialogs;
        this.clickListener = clickListener;
    }

    @Override
    public GroupsAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupsAdapter.GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_chat_list, null));
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.GroupViewHolder holder, int position) {
        holder.bindData(userGroups.get(position));
    }

    @Override
    public int getItemCount() {
        return userGroups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_image)
        CircleImageView imageView;
        @BindView(R.id.user_name)
        TextView userNameTv;
        GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final UserGroups userGroups){
            imageView.setImageResource(R.mipmap.goup_icon);
            userNameTv.setText(userGroups.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(userGroups);
                }
            });
        }

    }

}
