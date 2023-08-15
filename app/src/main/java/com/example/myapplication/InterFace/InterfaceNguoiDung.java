package com.example.myapplication.InterFace;

import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.Model.SanPham;

import java.util.List;

public interface InterfaceNguoiDung {
    void getDataSanPham(List<NguoiDung> list);
    void getNguoiDungLogin(NguoiDung nguoiDung);
    void getStatus(int status);
}
