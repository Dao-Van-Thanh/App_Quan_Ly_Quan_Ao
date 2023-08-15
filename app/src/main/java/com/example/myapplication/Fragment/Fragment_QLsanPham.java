package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.RecyclerViewAdapterQLSP;
import com.example.myapplication.InterFace.InterfaceSanPham;
import com.example.myapplication.Model.SanPham;
import com.example.myapplication.Presenter.SanPhamPresenter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class Fragment_QLsanPham extends Fragment {

    private View view;
    private AppCompatActivity activity;
    private RecyclerView recyclerView;
    private Context context;
    private RecyclerViewAdapterQLSP adapterQLSP;
    private String searchSP = "";



    public Fragment_QLsanPham() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ql_san_pham, container, false);
        anhXa();
        loadData();


        return view;
    }

    private void loadData() {
        SanPhamPresenter presenter = new SanPhamPresenter(context);
        presenter.getData(new InterfaceSanPham() {
            @Override
            public void getDataSanPham(List<SanPham> list) {
                if (!searchSP.isEmpty()) {
                    List<SanPham> sanPhamList = listSanPhamSearch(searchSP, list);
                    adapterQLSP.SendData(sanPhamList);
                } else {
                    adapterQLSP.SendData(list);
                }
            }

            @Override
            public void getMessage(String message) {
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                System.out.println(message);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterQLSP);
    }

    private void anhXa() {
        activity = (AppCompatActivity) getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_ql_sp_recyclerView);
        context = view.getContext();
        adapterQLSP = new RecyclerViewAdapterQLSP(context);
    }

    public void searchSanPham(String search) {
        searchSP = search;
        loadData();
    }


    private List<SanPham> listSanPhamSearch(String search, List<SanPham> ls) {
        List<SanPham> list = new ArrayList<>();
        for (SanPham sp : ls) {
            if (sp.getName().toLowerCase().contains(search)) {
                list.add(sp);
            }
        }
        return list;
    }

}