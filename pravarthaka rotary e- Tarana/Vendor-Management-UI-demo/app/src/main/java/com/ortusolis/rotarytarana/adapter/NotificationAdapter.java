package com.ortusolis.rotarytarana.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.fragment.NotificationFragment;
import com.ortusolis.rotarytarana.pojo.UserInfo;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ModuleViewHolder> {
    private ArrayList<UserInfo> dataList;
    Context context;
    private static final String TAG = "ContentItemAdapter";
    NotificationFragment notificationFragment;

    public NotificationAdapter(ArrayList<UserInfo> dataList, Context context, NotificationFragment notificationFragment) {
        this.dataList = dataList;
        this.context = context;
        this.notificationFragment = notificationFragment;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description;
        LinearLayout customerLay;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            customerLay = (LinearLayout) itemView.findViewById(R.id.customer3);
        }

    }

    @Override
    public ModuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_notification, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ModuleViewHolder holder, int position) {
        final UserInfo data = dataList.get(position);

        String userAs = "";
        if (data.getUserRoleId().equalsIgnoreCase("2")){
            userAs = "Distributor";
        }
        else if (data.getUserRoleId().equalsIgnoreCase("3")){
            userAs = "Employee";
        }
        else if (data.getUserRoleId().equalsIgnoreCase("4")){
            userAs = "Customer";
        }
        holder.title.setText(data.getFirstName() +" "+data.getLastName()+" registered as "+userAs);
        holder.customerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationFragment.approvalScreen(data);
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
}
