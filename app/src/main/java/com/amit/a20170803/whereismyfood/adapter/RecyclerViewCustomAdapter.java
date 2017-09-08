package com.amit.a20170803.whereismyfood.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amit.a20170803.whereismyfood.BranchesActivity;
import com.amit.a20170803.whereismyfood.R;
import com.amit.a20170803.whereismyfood.WSClasses.Menus;
import com.amit.a20170803.whereismyfood.WSClasses.Restaurant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.UserViewHolder>{

    private ArrayList<Restaurant> userList;
    ArrayList<Menus> myCart;
    String loggedUserId;
    private Context context;
    public RecyclerViewCustomAdapter(ArrayList<Restaurant> userList, Context context,ArrayList<Menus> myCart,String loggedUserId) {
        this.userList = userList;
        this.context = context;
        this.myCart = myCart;
        this.loggedUserId = loggedUserId;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cardview_layout, null);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final Restaurant user = userList.get(position);


        Picasso.with(context).load(user.getImgUrl()).into(holder.ivProfilePic, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
        holder.tvProfileName.setText(user.getName());
        holder.goAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goTosecondPage = new Intent(view.getContext(), BranchesActivity.class);
                goTosecondPage.putExtra("ResId",user.getId());
                goTosecondPage.putExtra("MyCart",myCart);
                goTosecondPage.putExtra("UserId",loggedUserId);
                context.startActivity(goTosecondPage);
            }
        });
        //holder.ivProfilePic.setImageResource(user.getImageResourceId());
        //holder.tvPhoneNumber.setText(user.getPhoneNumber());
        //holder.tvEmailId.setText(user.getEmailId());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvProfileName;
        TextView tvPhoneNumber;
        TextView tvEmailId;
        ImageButton goAddressBtn;
        public UserViewHolder(View itemView) {
            super(itemView);
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvProfileName = (TextView) itemView.findViewById(R.id.tvProfileName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
            goAddressBtn = (ImageButton) itemView.findViewById(R.id.go_address_btn);
        }
    }
}