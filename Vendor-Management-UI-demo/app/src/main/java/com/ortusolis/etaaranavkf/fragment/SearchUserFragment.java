package com.ortusolis.etaaranavkf.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.NetworkUtility.IResult;
import com.ortusolis.etaaranavkf.NetworkUtility.WebserviceController;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.adapter.UserAdapter;
import com.ortusolis.etaaranavkf.pojo.DistributorResponse;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchUserFragment extends Fragment {

    ActionBar action;
    private Toolbar toolbar;
    Spinner spinnerUserType,spinnerRoleType;
    ImageView searchButton;
    EditText searchEdit;
    ProgressDialog progressDialog;
    LinearLayout distributorCodeLayout,productIdLayout,productPriceLayout,searchLL;
    ArrayList<String> usernameList = new ArrayList<>();
    ArrayList<String> userTypeList = new ArrayList<>();
    ArrayList<String> rolenameList = new ArrayList<>();
    ArrayList<String> roleIdList = new ArrayList<>();
    boolean addUser = true;
    Gson gson;
    RecyclerView recyclerView;
    UserAdapter notificationAdapter;
    SharedPreferences sharedPreferences;

    public static SearchUserFragment newInstance() {

        Bundle args = new Bundle();
        SearchUserFragment fragment = new SearchUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_user,null);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        spinnerUserType = view.findViewById(R.id.spinnerUserType);
        spinnerRoleType = view.findViewById(R.id.spinnerRoleType);
        searchEdit = view.findViewById(R.id.searchEdit);
        searchButton = view.findViewById(R.id.searchButton);
        searchLL = view.findViewById(R.id.searchLL);
        recyclerView = view.findViewById(R.id.recyclerView);
        sharedPreferences = getActivity().getSharedPreferences("water_management",0);

        gson = new Gson();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        usernameList.add("First Name");
        usernameList.add("Last Name");
        userTypeList.add("UsersFirstname");
        userTypeList.add("UsersLastname");

        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1") || sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            usernameList.add("Role");
            userTypeList.add("UserRoleId");
        }

        if (!sharedPreferences.getString("userRoleId","").equalsIgnoreCase("2")){
            usernameList.add("Email Id");
            userTypeList.add("UsersEmailid");
        }


        ArrayAdapter userAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, usernameList);
        spinnerUserType.setAdapter(userAdapter);
        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (usernameList.get(i).equalsIgnoreCase("Role")){
                    spinnerRoleType.setVisibility(View.VISIBLE);
                    searchLL.setVisibility(View.GONE);
                }
                else {
                    spinnerRoleType.setVisibility(View.GONE);
                    searchLL.setVisibility(View.VISIBLE);
                }

                if (notificationAdapter!=null){
                    notificationAdapter.clearAll();
                }
                searchEdit.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rolenameList.add("Select Role");
        roleIdList.add("0");

        if (sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")) {
            rolenameList.add("Facilitator");
            roleIdList.add("2");
        }

        rolenameList.add("Partner");
        roleIdList.add("3");
        rolenameList.add("Donor");
        roleIdList.add("4");
        rolenameList.add("beneficiary");
        roleIdList.add("5");

        ArrayAdapter roleAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, rolenameList);

        spinnerRoleType.setAdapter(roleAdapter);

        spinnerRoleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i!=0) {
                    if (notificationAdapter != null) {
                        notificationAdapter.clearAll();
                    }
                    searchEdit.setText("");
                    getAllRegister(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllRegister(true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (spinnerRoleType.getSelectedItemPosition()!=0) {
            if (notificationAdapter != null) {
                notificationAdapter.clearAll();
            }
            searchEdit.setText("");
            getAllRegister(false);
        }
    }

    void getAllRegister(boolean typed){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        WebserviceController wss = new WebserviceController(getActivity());

        JSONObject requestObject = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchKey", userTypeList.get(spinnerUserType.getSelectedItemPosition()));
            jsonObject.put("searchvalue", typed ? searchEdit.getText().toString() : roleIdList.get(spinnerRoleType.getSelectedItemPosition()));
            jsonObject.put("distributorId", sharedPreferences.getString("userid", ""));
            requestObject.put("requestData",jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (notificationAdapter!=null){
            notificationAdapter.clearAll();
        }

        wss.postLoginVolley(Constants.getUserDetails, requestObject.toString(), new IResult() {
            @Override
            public void notifySuccess(String response, int statusCode) {
                try {
                    progressDialog.dismiss();
                    Log.e("getPlants response", response);

                    DistributorResponse distributorResponse = gson.fromJson(response, DistributorResponse.class);

                    if (distributorResponse.getResponseCode().equalsIgnoreCase("200")) {
                        notificationAdapter = new UserAdapter(distributorResponse.getResponseData().getUserDetails(),getActivity(),true, rolenameList.get(spinnerRoleType.getSelectedItemPosition()).equalsIgnoreCase("Distributor"));
                        recyclerView.setAdapter(notificationAdapter);

                    }

                    if (distributorResponse.getResponseData().getUserDetails().isEmpty()){
                        Toast.makeText(getActivity(), "No Results", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressDialog.dismiss();
                Crashlytics.logException(new Throwable(WebserviceController.returnErrorJson(error)));
            }
        });
    }
}
