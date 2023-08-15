package com.example.myapplication.Presenter;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.InterFace.InterfaceSanPham;
import com.example.myapplication.Model.SanPham;
import com.example.myapplication.Model.UrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SanPhamPresenter {
    private Context context;
    public String url = new UrlApi().url + "api/dataproducts";
    private List<SanPham> list = new ArrayList<>();

    public SanPhamPresenter(Context context) {
        this.context = context;
    }

    public void getData(InterfaceSanPham interfaceSanPham) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                try {
                                    JSONObject sanPhams = dataArray.getJSONObject(i);
                                    SanPham sanPham = new SanPham(
                                            sanPhams.getString("_id"),
                                            sanPhams.getString("image"),
                                            sanPhams.getString("name"),
                                            sanPhams.getString("price"),
                                            sanPhams.getString("color"),
                                            sanPhams.getString("type")
                                    );
                                    list.add(sanPham);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            interfaceSanPham.getDataSanPham(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        interfaceSanPham.getMessage(error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}
