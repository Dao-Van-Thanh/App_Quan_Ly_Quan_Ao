package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.InterFace.InterFaceControl;
import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.Model.UrlApi;
import com.example.myapplication.Presenter.NguoiDungPresanter;
import com.example.myapplication.R;
import com.example.myapplication.View.ActivityLogin;
import com.example.myapplication.View.ActivityThongTin;


public class Fragment_ThongTin extends Fragment {

    private View view;
    private ConstraintLayout layoutTT, layoutDoiMK;
    private Button btnLayoutTTDoiMK, btnQuayLai, btnDangXuat, btnThongTin, btnChagePass;
    private TextView tvFullName;
    private ImageView imageView;
    private boolean _Visibility = true;
    private InterFaceControl control;
    private Context context, contextActivity;
    private NguoiDung nguoiDung;
    private EditText edtPassOld, edtPassNew1, edtPassNew2;

    public Fragment_ThongTin() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thong_tin, container, false);

        anhXa();

        //SET thông tin người dùng
        if (nguoiDung.getFullname().isEmpty()) {
            tvFullName.setText("Xin Chào!  (●'◡'●)");
        } else {
            tvFullName.setText("Xin Chào! " + nguoiDung.getFullname());
        }
        Glide.with(context)
                .load(new UrlApi().url + "/Image/" + nguoiDung.getImage())
                .error(R.drawable.error_image_generic) // Resource ID của hình ảnh thông báo lỗi
                .into(imageView);


        // Tùy chỉnh hiệu ứng khi hiển thị
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);

        //điều khiển ẩn hiện
        control = new InterFaceControl() {
            @Override
            public void isVisibility() {
                if (_Visibility) {
                    layoutTT.setVisibility(View.VISIBLE);
                    layoutDoiMK.setVisibility(View.GONE);

                    layoutTT.startAnimation(fadeInAnimation);
                    layoutDoiMK.startAnimation(fadeOutAnimation);
                } else {
                    layoutTT.setVisibility(View.GONE);
                    layoutDoiMK.setVisibility(View.VISIBLE);

                    layoutTT.startAnimation(fadeOutAnimation);
                    layoutDoiMK.startAnimation(fadeInAnimation);

                }
            }
        };


        LayoutThongTin();

        LayoutDoiMatKhau();


        return view;
    }

    private void LayoutDoiMatKhau() {
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _Visibility = !_Visibility;
                control.isVisibility();
            }
        });
        btnChagePass.setOnClickListener((view) -> {
            kiemTraPass();
        });
    }

    private void kiemTraPass() {
        String passOld = edtPassOld.getText().toString();
        String passNew1 = edtPassNew1.getText().toString();
        String passNew2 = edtPassNew2.getText().toString();
        //Kiểm tra trống
        if (passOld.isEmpty()) {
            Toast.makeText(context, "Mật khẩu cũ không hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (passNew1.isEmpty()) {
            Toast.makeText(context, "Mật khẩu mới không hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (passNew2.isEmpty()) {
            Toast.makeText(context, "Nhập lại mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
            //Kiểm tra giống nhau
            if (!passNew1.equals(passNew2)) {
                Toast.makeText(context, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            } else if (passNew1.equals(passOld)) {
                Toast.makeText(context, "Mật khẩu mới không được trùng với mật khẩu cũ", Toast.LENGTH_SHORT).show();
            } else {
                //kiểm tra mật khẩu cũ có đúng không
                if (!passOld.equals(nguoiDung.getPassword())) {
                    Toast.makeText(context, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                } else {
                    doiMatKhau(passNew2);
                }
            }
        }
    }

    private void doiMatKhau(String passNew) {
        nguoiDung.setPassword(passNew);
        NguoiDungPresanter presanter = new NguoiDungPresanter(context);
        presanter.updateNguoiDung(nguoiDung);
    }

    private void LayoutThongTin() {
        btnThongTin.setOnClickListener((view) -> {
            Intent intent = new Intent(contextActivity, ActivityThongTin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("NguoiDung", nguoiDung);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnLayoutTTDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _Visibility = !_Visibility;
                control.isVisibility();
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(contextActivity, ActivityLogin.class));
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contextActivity = getContext();
    }

    private void anhXa() {
        layoutTT = view.findViewById(R.id.fragment_thong_tin_layout_tt);
        layoutDoiMK = view.findViewById(R.id.fragment_thong_tin_layout_doi_mk);
        btnLayoutTTDoiMK = view.findViewById(R.id.fragment_thong_tin_btn_dmk);
        btnQuayLai = view.findViewById(R.id.fragment_thong_tin_btn_quay_lai);
        btnDangXuat = view.findViewById(R.id.fragment_thong_tin_btn_dang_xuat);
        tvFullName = view.findViewById(R.id.fragment_thong_tin_tv_fullname);
        imageView = view.findViewById(R.id.fragment_thong_tin_imageView);
        btnThongTin = view.findViewById(R.id.fragment_thong_tin_tt);
        edtPassOld = view.findViewById(R.id.fragment_thong_tin_edt_mk_cu);
        edtPassNew1 = view.findViewById(R.id.fragment_thong_tin_edt_mk_moi);
        edtPassNew2 = view.findViewById(R.id.fragment_thong_tin_edt_nhap_lai_mk);
        btnChagePass = view.findViewById(R.id.fragment_thong_tin_btn_doi_mk);
        context = view.getContext();

    }

    public void getData(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }
}