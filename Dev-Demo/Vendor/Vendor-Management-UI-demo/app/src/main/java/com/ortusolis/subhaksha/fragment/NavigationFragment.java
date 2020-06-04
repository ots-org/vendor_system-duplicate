package com.ortusolis.subhaksha.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ortusolis.subhaksha.Activity.AddProductActivity;
import com.ortusolis.subhaksha.Activity.AssignedOrderListActivity;
import com.ortusolis.subhaksha.Activity.BillListActivity;
import com.ortusolis.subhaksha.Activity.CustomerLedgerReportActivity;
import com.ortusolis.subhaksha.Activity.CustomerOutstandingReportActivity;
import com.ortusolis.subhaksha.Activity.EmployeeOrderAssignedListActivity;
import com.ortusolis.subhaksha.Activity.LoginActivity;
import com.ortusolis.subhaksha.Activity.MainActivity;
import com.ortusolis.subhaksha.Activity.MyBillActivity;
import com.ortusolis.subhaksha.Activity.NotificationActivity;
import com.ortusolis.subhaksha.Activity.OrderListActivity;
import com.ortusolis.subhaksha.Activity.OrderReportActivity;
import com.ortusolis.subhaksha.Activity.PasswordActivity;
import com.ortusolis.subhaksha.Activity.ProductTransactionReportActivity;
import com.ortusolis.subhaksha.Activity.SchedulerActivity;
import com.ortusolis.subhaksha.Activity.StatusListActivity;
import com.ortusolis.subhaksha.Activity.TransferOrderActivity;
import com.ortusolis.subhaksha.Activity.UpdateProductStockActivity;
import com.ortusolis.subhaksha.Activity.UserActivity;
import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.app.Config;

import org.jetbrains.annotations.Nullable;

public class NavigationFragment extends Fragment {

    TextView mastersHeader,stockHeader,ordersHeader,billsHeader,reportsHeader;
    TextView notification,list_products,users,products,updateStock,closeOrder,salesVoucher,scheduler,add,assign,transferOrders,generateBill,myBills,stockReport,orderReport,billReport,customerLedgerReport,customerOutstandingReport,logout;
   //code raghuram
    TextView changepassword;
    TextView changepasswordGlobal;
    //code raghuram
    LinearLayout mastersLL,stockLL,ordersLL,billsLL,reportsLL,myOrder,productsLL;
    TextView nav_header_textView;
    SharedPreferences sharedPreferences;
    ImageView imageView;

    public static NavigationFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NavigationFragment fragment = new NavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation,null);

        sharedPreferences = getActivity().getSharedPreferences("water_management",0);

        mastersHeader = view.findViewById(R.id.masters);
        stockHeader = view.findViewById(R.id.stock);
        ordersHeader = view.findViewById(R.id.orders);
        billsHeader = view.findViewById(R.id.bills);
        reportsHeader = view.findViewById(R.id.reports);
        imageView = view.findViewById(R.id.nav_header_imageView);

        nav_header_textView = view.findViewById(R.id.nav_header_textView);
        notification = view.findViewById(R.id.notification);
        productsLL = view.findViewById(R.id.productsLL);
        list_products = view.findViewById(R.id.list_products);
        users = view.findViewById(R.id.users);
        products = view.findViewById(R.id.products);
        //code raghuram ots
        changepassword = view.findViewById(R.id.changepassword);
        changepasswordGlobal =view.findViewById(R.id.changepasswordGlobal);
        //code raghuram ots
        updateStock = view.findViewById(R.id.updateStock);
        closeOrder = view.findViewById(R.id.closeOrder);
        salesVoucher = view.findViewById(R.id.salesVoucher);
        scheduler = view.findViewById(R.id.scheduler);
        add = view.findViewById(R.id.add);
        myOrder = view.findViewById(R.id.myOrder);
        assign = view.findViewById(R.id.assign);
        transferOrders = view.findViewById(R.id.transferOrders);
        myBills = view.findViewById(R.id.myBills);
        generateBill = view.findViewById(R.id.generateBill);
        stockReport = view.findViewById(R.id.stockReport);
        orderReport = view.findViewById(R.id.orderReport);
        billReport = view.findViewById(R.id.billReport);
        customerLedgerReport = view.findViewById(R.id.customerLedgerReport);
        customerOutstandingReport = view.findViewById(R.id.customerOutstandingReport);
        logout = view.findViewById(R.id.logout);

        mastersLL = view.findViewById(R.id.mastersLL);
        stockLL = view.findViewById(R.id.stockLL);
        ordersLL = view.findViewById(R.id.ordersLL);
        billsLL = view.findViewById(R.id.billsLL);
        reportsLL = view.findViewById(R.id.reportsLL);

        String userRole = "Administrator";

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            userRole = "Distributor";
            products.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
            salesVoucher.setVisibility(View.GONE);
            myBills.setVisibility(View.GONE);
            stockHeader.setVisibility(View.VISIBLE);
            transferOrders.setVisibility(View.GONE);
            stockReport.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
            billReport.setVisibility(View.GONE);

        }
        else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
            userRole = "Employee";
            notification.setVisibility(View.GONE);
            mastersHeader.setVisibility(View.GONE);
            stockHeader.setVisibility(View.GONE);
            closeOrder.setVisibility(View.GONE);
            salesVoucher.setVisibility(View.GONE);
            assign.setVisibility(View.GONE);
            customerLedgerReport.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            myBills.setVisibility(View.GONE);
            billsHeader.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
            generateBill.setVisibility(View.GONE);
            //
            changepasswordGlobal.setVisibility(View.VISIBLE);
            //
        }
        else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
            userRole = "Customer";
            notification.setVisibility(View.GONE);
            mastersHeader.setVisibility(View.GONE);
            stockHeader.setVisibility(View.GONE);
            assign.setVisibility(View.GONE);
            reportsHeader.setVisibility(View.GONE);
            myOrder.setVisibility(View.VISIBLE);
            customerLedgerReport.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            generateBill.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
            //
            changepasswordGlobal.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
            stockReport.setVisibility(View.GONE);
            orderReport.setVisibility(View.GONE);
            billReport.setVisibility(View.GONE);
            //
        }
        else {
            stockHeader.setVisibility(View.GONE);
            ordersHeader.setVisibility(View.GONE);
            billsHeader.setVisibility(View.GONE);
            reportsHeader.setVisibility(View.VISIBLE);
            productsLL.setVisibility(View.VISIBLE);
            stockReport.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
            billReport.setVisibility(View.GONE);
        }
        nav_header_textView.setText(sharedPreferences.getString("username","")+" ( "+userRole+" )");

        Bitmap decodedByte = null;

        if (sharedPreferences.contains("profilePic") && !TextUtils.isEmpty(sharedPreferences.getString("profilePic",""))) {
            byte[] decodedString = Base64.decode(sharedPreferences.getString("profilePic",""), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        if (decodedByte != null) {
            imageView.setImageBitmap(decodedByte);
        }

        mastersHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mastersLL.getVisibility()== View.VISIBLE){
                    mastersLL.setVisibility(View.GONE);
                }
                else {
                    mastersLL.setVisibility(View.VISIBLE);
                }

            }
        });

        stockHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stockLL.getVisibility()== View.VISIBLE){
                    stockLL.setVisibility(View.GONE);
                }
                else {
                    stockLL.setVisibility(View.VISIBLE);
                }

            }
        });

        ordersHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ordersLL.getVisibility()== View.VISIBLE){
                    ordersLL.setVisibility(View.GONE);
                }
                else {
                    ordersLL.setVisibility(View.VISIBLE);
                }

            }
        });

        billsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billsLL.getVisibility()== View.VISIBLE){
                    billsLL.setVisibility(View.GONE);
                }
                else {
                    billsLL.setVisibility(View.VISIBLE);
                }

            }
        });

        reportsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportsLL.getVisibility()== View.VISIBLE){
                    reportsLL.setVisibility(View.GONE);
                }
                else {
                    reportsLL.setVisibility(View.VISIBLE);
                }

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
                    ((MainActivity)getActivity()).loadNotificationsScreen();
                }
                else {
                    startActivity(new Intent(getActivity(), NotificationActivity.class));
                }
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        list_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadProducts();
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddProductActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
       // code by Raghuram Ots
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChangePassword = new Intent(getActivity(), PasswordActivity.class);
                ChangePassword.putExtra("ChangePassword", "ChangePassword");
                startActivity(ChangePassword);
               // startActivity(new Intent(getActivity(), PasswordActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        // code by Raghuram Ots
        changepasswordGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changepasswordGlobal = new Intent(getActivity(), PasswordActivity.class);
                changepasswordGlobal.putExtra("changepasswordGlobal", "changepasswordGlobal");
                startActivity(changepasswordGlobal);
                // startActivity(new Intent(getActivity(), PasswordActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        //code by Raghuram Ots

        updateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProductStockActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        generateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BillListActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

       myBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyBillActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        stockReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductTransactionReportActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        orderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderReportActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SchedulerActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StatusListActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        billReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyBillActivity.class));
                //startActivity(new Intent(getActivity(), BillReportActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        customerLedgerReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerLedgerReportActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        customerOutstandingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerOutstandingReportActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        closeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeeOrderAssignedListActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        salesVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AssignedOrderListActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        transferOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TransferOrderActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pushNotification = new Intent(Config.LOGOUT);
                getActivity().sendBroadcast(pushNotification);

                sharedPreferences.edit().clear().commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });



        return view;
    }
}
