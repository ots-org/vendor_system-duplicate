package com.ortusolis.subhaksha.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ortusolis.subhaksha.R;

public class ReportFragment extends Fragment {

    public static ReportFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment,null);
        return view;
    }



}