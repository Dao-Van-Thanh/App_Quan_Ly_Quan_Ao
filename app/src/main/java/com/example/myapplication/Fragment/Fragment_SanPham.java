package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.RecyclerGridViewAdapterSP;
import com.example.myapplication.InterFace.InterfaceSanPham;
import com.example.myapplication.Model.SanPham;
import com.example.myapplication.Presenter.SanPhamPresenter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class Fragment_SanPham extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private Context context;
    private RecyclerGridViewAdapterSP adapterSP;
    private String searchSP = "";

    public Fragment_SanPham() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_san_pham, container, false);
        anhXa();

        loadData();




        return view;
    }

    private void loadData() {

        //set recyclerView dạng lưới
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        SanPhamPresenter presenter = new SanPhamPresenter(context);
        presenter.getData(new InterfaceSanPham() {
            @Override
            public void getDataSanPham(List<SanPham> list) {
                if (!searchSP.isEmpty()) {
                    List<SanPham> sanPhamList = listSanPhamSearch(searchSP, list);
                    adapterSP.sendData(sanPhamList);
                } else {
                    adapterSP.sendData(list);
                }
            }

            @Override
            public void getMessage(String message) {
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                System.out.println(message);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterSP);

    }



    private void anhXa() {

        context = view.getContext();
        adapterSP = new RecyclerGridViewAdapterSP(context);
        recyclerView = view.findViewById(R.id.fragment_san_pham_recyclerView);
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