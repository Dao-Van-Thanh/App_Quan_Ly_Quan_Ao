package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;


public class Fragment_QLnguoiDung extends Fragment {

    View view;

    public Fragment_QLnguoiDung() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ql_nguoi_dung, container, false);



        return view;
    }
    public void getData(String s){
        System.out.println(s);
    }
}