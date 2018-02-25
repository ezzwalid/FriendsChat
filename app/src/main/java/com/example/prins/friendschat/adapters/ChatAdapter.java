package com.example.prins.friendschat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.prins.friendschat.Dtos.Msg;
import com.example.prins.friendschat.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by prins on 2/24/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    ArrayList<Msg> msgs;

    public ChatAdapter(ArrayList<Msg> msgs) {
        this.msgs = msgs;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, null));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.bindData(msgs.get(position));
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_image)
        CircleImageView imageView;
        @BindView(R.id.msg_tv)
        TextView msgTv;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Msg msg){
            if (msg.getUser().getImage_url() != null){
                Picasso.with(imageView.getContext()).load(msg.getUser().getImage_url()).into(imageView);
            }
            else {
                imageView.setImageResource(R.mipmap.frinds_logo);
            }
            msgTv.setText(msg.getMessage());
        }

    }

    public void addMsg(Msg msg){
        this.msgs.add(msg);
        notifyDataSetChanged();
    }
}
