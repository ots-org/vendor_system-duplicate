package com.ortusolis.rotarytarana.fragment;

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

import com.ortusolis.rotarytarana.Activity.AddProductActivity;
import com.ortusolis.rotarytarana.Activity.AddSubCategory;
import com.ortusolis.rotarytarana.Activity.AssignedOrderListActivity;
import com.ortusolis.rotarytarana.Activity.BillListActivity;
import com.ortusolis.rotarytarana.Activity.ConfirmRequest;
import com.ortusolis.rotarytarana.Activity.CustomerLedgerReportActivity;
import com.ortusolis.rotarytarana.Activity.CustomerOutstandingReportActivity;
import com.ortusolis.rotarytarana.Activity.DonationActivity;
import com.ortusolis.rotarytarana.Activity.DonationReport;
import com.ortusolis.rotarytarana.Activity.DonationStatus;
import com.ortusolis.rotarytarana.Activity.EmployeeOrderAssignedListActivity;
import com.ortusolis.rotarytarana.Activity.LoginActivity;
import com.ortusolis.rotarytarana.Activity.MainActivity;
import com.ortusolis.rotarytarana.Activity.MyBillActivity;
import com.ortusolis.rotarytarana.Activity.NotificationActivity;
import com.ortusolis.rotarytarana.Activity.OrderListActivity;
import com.ortusolis.rotarytarana.Activity.OrderReportActivity;
import com.ortusolis.rotarytarana.Activity.PasswordActivity;
import com.ortusolis.rotarytarana.Activity.ProductTransactionReportActivity;
import com.ortusolis.rotarytarana.Activity.SchedulerActivity;
import com.ortusolis.rotarytarana.Activity.StatusListActivity;
import com.ortusolis.rotarytarana.Activity.SwitchRole;
import com.ortusolis.rotarytarana.Activity.TransferOrderActivity;
import com.ortusolis.rotarytarana.Activity.UpdateProductStockActivity;
import com.ortusolis.rotarytarana.Activity.UserActivity;
import com.ortusolis.rotarytarana.Activity.UserProfileActivity;
import com.ortusolis.rotarytarana.R;
import com.ortusolis.rotarytarana.app.Config;

import org.jetbrains.annotations.Nullable;

public class NavigationFragment extends Fragment {

    TextView mastersHeader,stockHeader,ordersHeader,billsHeader,reportsHeader;
    TextView notification,list_products,users,products,updateStock,closeOrder,salesVoucher,scheduler,add,assign,transferOrders,generateBill,myBills,stockReport,orderReport,billReport,customerLedgerReport,customerOutstandingReport,logout,switchRoll,profile,SubProducts,donation,donationReport,donationReportCustomer,assignRequest,empAssignRequest,requestProductCustomer,confirmRequestProduct,donationStatus,bills;
    TextView changepassword;
    TextView changepasswordGlobal;
    LinearLayout mastersLL,stockLL,ordersLL,billsLL,reportsLL,myOrder,myRequests,productsLL;
    TextView nav_header_textView;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    View requestProductCustomerView;

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
        SubProducts = view.findViewById(R.id.SubProducts);
        donation = view.findViewById(R.id.donation);
        donationStatus= view.findViewById(R.id.donationStatus);
        donationReportCustomer= view.findViewById(R.id.donationReportCustomer);
        assignRequest = view.findViewById(R.id.assignRequest);
        empAssignRequest= view.findViewById(R.id.empAssignRequest);
        requestProductCustomer= view.findViewById(R.id.requestProductCustomer);
        requestProductCustomerView= view.findViewById(R.id.requestProductCustomerView);
        confirmRequestProduct= view.findViewById(R.id.confirmRequestProduct);
        changepassword = view.findViewById(R.id.changepassword);
        changepasswordGlobal =view.findViewById(R.id.changepasswordGlobal);
        updateStock = view.findViewById(R.id.updateStock);
        closeOrder = view.findViewById(R.id.closeOrder);
        salesVoucher = view.findViewById(R.id.salesVoucher);
        scheduler = view.findViewById(R.id.scheduler);
        add = view.findViewById(R.id.add);
        myOrder = view.findViewById(R.id.myOrder);
        myRequests= view.findViewById(R.id.myRequest);
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
        switchRoll=view.findViewById(R.id.switchRoll);
        profile=view.findViewById(R.id.profile);
        donationReport=view.findViewById(R.id.donationReport);
        mastersLL = view.findViewById(R.id.mastersLL);
        stockLL = view.findViewById(R.id.stockLL);
        ordersLL = view.findViewById(R.id.ordersLL);
        billsLL = view.findViewById(R.id.billsLL);
        reportsLL = view.findViewById(R.id.reportsLL);

        String userRole = "Administrator";
        if (sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("1")||sharedPreferences.contains("userSwitchRoleId") && sharedPreferences.getString("userSwitchRoleId","").equalsIgnoreCase("2")){
            switchRoll.setVisibility(View.VISIBLE);
        }else {
            switchRoll.setVisibility(View.GONE);
        }

        if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            userRole = "Facilitator";
            products.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
            SubProducts.setVisibility(View.GONE);
            scheduler.setVisibility(View.VISIBLE);
            salesVoucher.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            myBills.setVisibility(View.GONE);
            billsHeader.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            billReport.setVisibility(View.GONE);
            stockHeader.setVisibility(View.VISIBLE);
            transferOrders.setVisibility(View.VISIBLE);
            confirmRequestProduct.setVisibility(View.GONE);
        }
        else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
            userRole = "Employee";
            assignRequest.setText("Assigned Request");
            notification.setVisibility(View.GONE);
            mastersHeader.setVisibility(View.GONE);
            stockHeader.setVisibility(View.GONE);
            closeOrder.setVisibility(View.GONE);
            billReport.setVisibility(View.GONE);
            salesVoucher.setVisibility(View.VISIBLE);
            assign.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            customerLedgerReport.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            myBills.setVisibility(View.GONE);
            billsHeader.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
            generateBill.setVisibility(View.GONE);
            donationStatus.setVisibility(View.GONE);
            changepasswordGlobal.setVisibility(View.VISIBLE);
            empAssignRequest.setVisibility(View.VISIBLE);
        }
        else if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("4")){
            userRole = "Donor/Requestor";
            notification.setVisibility(View.GONE);
            mastersHeader.setVisibility(View.GONE);
            stockHeader.setVisibility(View.GONE);
            assign.setVisibility(View.GONE);
            reportsHeader.setVisibility(View.GONE);
            myOrder.setVisibility(View.VISIBLE);
            myRequests.setVisibility(View.VISIBLE);
            customerLedgerReport.setVisibility(View.GONE);
            customerOutstandingReport.setVisibility(View.GONE);
            generateBill.setVisibility(View.GONE);
            scheduler.setVisibility(View.GONE);
            donationReportCustomer.setVisibility(View.VISIBLE);
            requestProductCustomer.setVisibility(View.VISIBLE);
            requestProductCustomerView.setVisibility(View.VISIBLE);
            assignRequest.setVisibility(View.GONE);
            empAssignRequest.setVisibility(View.GONE);
            donationStatus.setVisibility(View.GONE);
            changepasswordGlobal.setVisibility(View.VISIBLE);
            billsHeader.setVisibility(View.GONE);
        }
        else {
            stockHeader.setVisibility(View.GONE);
            ordersHeader.setVisibility(View.GONE);
            billsHeader.setVisibility(View.GONE);
            reportsHeader.setVisibility(View.VISIBLE);
            customerOutstandingReport.setVisibility(View.GONE);
            notification.setVisibility(View.GONE);
            productsLL.setVisibility(View.VISIBLE);
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
        SubProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddSubCategory.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DonationActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        donationReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DonationReport.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        donationReportCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DonationReport.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        donationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donationStatus = new Intent(getActivity(), DonationStatus.class);
                startActivity(donationStatus);
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        assignRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("3")){
                    Intent assignRequest = new Intent(getActivity(), DonationStatus.class);
                    assignRequest.putExtra("assignRequest", sharedPreferences.getString("userRoleId",""));
                    startActivity(assignRequest);
                    ((MainActivity)getActivity()).closeDrawer();
                }else {
                    Intent assignRequest = new Intent(getActivity(), OrderListActivity.class);
                    assignRequest.putExtra("assignRequest", sharedPreferences.getString("userRoleId", ""));
                    startActivity(assignRequest);
                    ((MainActivity) getActivity()).closeDrawer();
                }
            }
        });
        empAssignRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent empAssignRequest = new Intent(getActivity(), AssignedOrderListActivity.class);
                empAssignRequest.putExtra("deliverDonation", "deliverDonation");
                startActivity(empAssignRequest);
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        switchRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SwitchRole.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        requestProductCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requestProductCustomer = new Intent(getActivity(), AddProductActivity.class);
                requestProductCustomer.putExtra("requestProductCustomer", "requestProductCustomer");
                startActivity(requestProductCustomer);
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        confirmRequestProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmRequestProduct = new Intent(getActivity(), ConfirmRequest.class);
                startActivity(confirmRequestProduct);
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChangePassword = new Intent(getActivity(), PasswordActivity.class);
                ChangePassword.putExtra("ChangePassword", "ChangePassword");
                startActivity(ChangePassword);
                ((MainActivity)getActivity()).closeDrawer();
            }
        });
        changepasswordGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changepasswordGlobal = new Intent(getActivity(), PasswordActivity.class);
                changepasswordGlobal.putExtra("changepasswordGlobal", "changepasswordGlobal");
                startActivity(changepasswordGlobal);
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

        myRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myRequests = new Intent(getActivity(), StatusListActivity.class);
                myRequests.putExtra("myRequests", "myRequests");
                startActivity(myRequests);
                ((MainActivity)getActivity()).closeDrawer();
            }
        });


        billReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyBillActivity.class));
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
