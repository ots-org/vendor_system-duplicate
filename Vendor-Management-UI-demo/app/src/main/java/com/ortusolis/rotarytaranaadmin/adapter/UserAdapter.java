package com.ortusolis.rotarytaranaadmin.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ortusolis.rotarytaranaadmin.Activity.ListMappedUsersList;
import com.ortusolis.rotarytaranaadmin.Activity.MapsMarkerActivity;
import com.ortusolis.rotarytaranaadmin.R;
import com.ortusolis.rotarytaranaadmin.Activity.UpdateUserActivity;
import com.ortusolis.rotarytaranaadmin.pojo.UserInfo;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ModuleViewHolder> {
    private ArrayList<UserInfo> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";
    SharedPreferences sharedPreferences;
    boolean searchBool = false;
    boolean isDistributor = false;

    public UserAdapter(ArrayList<UserInfo> dataList, Context context,boolean search,boolean isDistributor) {
        this.dataList = dataList;
        this.context = context;
        this.searchBool = search;
        this.isDistributor = isDistributor;
        sharedPreferences = context.getSharedPreferences("water_management",0);
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        public TextView name, addressText,email,customerText,employeeText;
        LinearLayout customerLay,distributorLL;
        ImageButton editImage,myLocation;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameText);
            addressText = (TextView) itemView.findViewById(R.id.addressText);
            email = (TextView) itemView.findViewById(R.id.emailText);
            customerText = (TextView) itemView.findViewById(R.id.customerText);
            employeeText = (TextView) itemView.findViewById(R.id.employeeText);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
            distributorLL = (LinearLayout) itemView.findViewById(R.id.distributorLL);
            editImage = (ImageButton) itemView.findViewById(R.id.editImage);
            myLocation = (ImageButton) itemView.findViewById(R.id.myLocation);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_users, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ModuleViewHolder holder, int position) {
        final UserInfo data = dataList.get(position);

        holder.name.setText(data.getFirstName() +" "+data.getLastName());
        holder.addressText.setText(data.getAddress1());
        holder.email.setText(data.getEmailId());
        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, data.getFirstName() +" "+data.getLastName()+" selected", Toast.LENGTH_LONG).show();
            }
        });

        if (data.getUserRoleId().equalsIgnoreCase("3")){
            holder.myLocation.setVisibility(View.VISIBLE);
        }
        else {
            holder.myLocation.setVisibility(View.GONE);
        }

        if (searchBool && isDistributor){
            holder.distributorLL.setVisibility(View.VISIBLE);
        }
        else {
            holder.distributorLL.setVisibility(View.GONE);
        }

        holder.customerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListMappedUsersList.class);
                intent.putExtra("customer",true);
                intent.putExtra("username",data.getFirstName()+" "+data.getLastName());
                intent.putExtra("userId",data.getUserId());
                context.startActivity(intent);
            }
        });

        holder.employeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListMappedUsersList.class);
                intent.putExtra("customer",false);
                intent.putExtra("username",data.getFirstName()+" "+data.getLastName());
                intent.putExtra("userId",data.getUserId());
                context.startActivity(intent);
            }
        });

        holder.myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsMarkerActivity.class);
                intent.putExtra("userId",data.getUserId());
                context.startActivity(intent);
            }
        });


        holder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateUserActivity.class);
                intent.putExtra("addUser",data);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return 0;
    }

    public void clearAll(){
        dataList.clear();
        notifyDataSetChanged();
    }

}
