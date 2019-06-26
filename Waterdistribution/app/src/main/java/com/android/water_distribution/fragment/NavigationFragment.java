package com.android.water_distribution.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.water_distribution.Activity.AddProductActivity;
import com.android.water_distribution.Activity.AssignedOrderListActivity;
import com.android.water_distribution.Activity.BillListActivity;
import com.android.water_distribution.Activity.CustomerLedgerReportActivity;
import com.android.water_distribution.Activity.CustomerOutstandingReportActivity;
import com.android.water_distribution.Activity.CustomerStatusListFragment;
import com.android.water_distribution.Activity.EmployeeOrderAssignedListActivity;
import com.android.water_distribution.Activity.LoginActivity;
import com.android.water_distribution.Activity.MainActivity;
import com.android.water_distribution.Activity.MyBillActivity;
import com.android.water_distribution.Activity.NotificationActivity;
import com.android.water_distribution.Activity.OrderListActivity;
import com.android.water_distribution.Activity.OrderReportActivity;
import com.android.water_distribution.Activity.SchedulerActivity;
import com.android.water_distribution.Activity.StatusListActivity;
import com.android.water_distribution.R;
import com.android.water_distribution.Activity.ProductTransactionReportActivity;
import com.android.water_distribution.Activity.UpdateProductStockActivity;
import com.android.water_distribution.Activity.UserActivity;
import com.android.water_distribution.service.UserService;

public class NavigationFragment extends Fragment {

    TextView mastersHeader,stockHeader,ordersHeader,billsHeader,reportsHeader;
    TextView notification,users,products,updateStock,closeOrder,salesVoucher,scheduler,add,update,assign,generateBill,myBills,stockReport,orderReport,billReport,customerLedgerReport,customerOutstandingReport,logout;
    LinearLayout mastersLL,stockLL,ordersLL,billsLL,reportsLL,myOrder;
    TextView nav_header_textView;
    SharedPreferences sharedPreferences;

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

        nav_header_textView = view.findViewById(R.id.nav_header_textView);
        notification = view.findViewById(R.id.notification);
        users = view.findViewById(R.id.users);
        products = view.findViewById(R.id.products);
        updateStock = view.findViewById(R.id.updateStock);
        closeOrder = view.findViewById(R.id.closeOrder);
        salesVoucher = view.findViewById(R.id.salesVoucher);
        scheduler = view.findViewById(R.id.scheduler);
        add = view.findViewById(R.id.add);
        myOrder = view.findViewById(R.id.myOrder);
        update = view.findViewById(R.id.update);
        assign = view.findViewById(R.id.assign);
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
            scheduler.setVisibility(View.VISIBLE);
            salesVoucher.setVisibility(View.VISIBLE);
            myBills.setVisibility(View.GONE);
        }
        else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
            userRole = "Employee";
            notification.setVisibility(View.GONE);
            mastersHeader.setVisibility(View.GONE);
            stockHeader.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            closeOrder.setVisibility(View.GONE);
            salesVoucher.setVisibility(View.VISIBLE);
            assign.setVisibility(View.GONE);
            customerLedgerReport.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            myBills.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
            generateBill.setVisibility(View.GONE);
        }
        else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
            userRole = "Customer";
            notification.setVisibility(View.GONE);
            mastersHeader.setVisibility(View.GONE);
            stockHeader.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            assign.setVisibility(View.GONE);
            reportsHeader.setVisibility(View.GONE);
            myOrder.setVisibility(View.VISIBLE);
            customerLedgerReport.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            generateBill.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
        }
        else {
            stockHeader.setVisibility(View.GONE);
            ordersHeader.setVisibility(View.GONE);
            billsHeader.setVisibility(View.GONE);
            reportsHeader.setVisibility(View.GONE);
        }
        nav_header_textView.setText(sharedPreferences.getString("username","")+" ( "+userRole+" )");

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

        update.setOnClickListener(new View.OnClickListener() {
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().stopService(new Intent(getActivity(), UserService.class));

                sharedPreferences.edit().clear().commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });



        return view;
    }
}
