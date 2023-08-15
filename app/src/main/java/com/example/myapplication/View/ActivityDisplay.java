package com.example.myapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.myapplication.Adapter.ViewPager2Adapter;
import com.example.myapplication.Fragment.Fragment_QLnguoiDung;
import com.example.myapplication.Fragment.Fragment_QLsanPham;
import com.example.myapplication.Fragment.Fragment_SanPham;
import com.example.myapplication.Fragment.Fragment_ThongTin;
import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityDisplay extends AppCompatActivity {
    ViewPager2 viewPager2;
    BottomNavigationBar bottomNavigationBar;
    List<Fragment> lsFragment = new ArrayList<>();
    ImageView searchImageView;
    EditText edtTimKiem;
    Context context = this;
    LinearLayout layout;
    Fragment_SanPham fragment_sanPham;
    Fragment_ThongTin fragment_thongTin;
    Fragment_QLnguoiDung fragment_qLnguoiDung;
    Fragment_QLsanPham fragment_qLsanPham;
    private int isPos = 0;
    private NguoiDung nguoiDung;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        nguoiDung = new NguoiDung(
                intent.getStringExtra("id"),
                intent.getStringExtra("image"),
                intent.getStringExtra("fullname"),
                intent.getStringExtra("email"),
                intent.getStringExtra("password"),
                intent.getStringExtra("admin")
        );
        anhXa();
        fragment_thongTin.getData(nguoiDung);

        //bottom navigation bar
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_shopping_cart_24, "Sản Phẩm"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_chevron_left_24, "QL Sản Phẩm"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_chevron_right_24, "QL Người dùng"))
                .addItem(new BottomNavigationItem(R.drawable.ic_baseline_settings_24, "Thông tin"))
                .setActiveColor("#FF3333")
                .setInActiveColor("#000000")
//                .setBarBackgroundColor("#FF3333")
                .initialise();

        //ViewPager2

        lsFragment.add(fragment_sanPham);
        lsFragment.add(fragment_qLsanPham);
//        lsFragment.add(fragment_qLnguoiDung);
        lsFragment.add(fragment_thongTin);

        ViewPager2Adapter adapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle(), lsFragment);
        viewPager2.setAdapter(adapter);

        //chuyển tabs
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationBar.selectTab(position);
                isPos = position;
            }
        });
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager2.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        //sự kiện slick
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchEditText(); // Hiển thị EditText tìm kiếm
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                guiDuLieu(isPos, s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void anhXa() {
        viewPager2 = (ViewPager2) findViewById(R.id.viewPager2);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        searchImageView = (ImageView) findViewById(R.id.toolbar_tim_kiem);
        edtTimKiem = (EditText) findViewById(R.id.toolbar_search_editText);
        layout = (LinearLayout) findViewById(R.id.display_linear_layout);
        fragment_sanPham = new Fragment_SanPham();
        fragment_qLsanPham = new Fragment_QLsanPham();
        fragment_qLnguoiDung = new Fragment_QLnguoiDung();
        fragment_thongTin = new Fragment_ThongTin();
    }

    private void showSearchEditText() {
        if (edtTimKiem.getVisibility() > 0) {
            edtTimKiem.setVisibility(View.VISIBLE); // Hiển thị EditText tìm kiếm

            // Tùy chỉnh hiệu ứng khi hiển thị EditText tìm kiếm (nếu cần)
            Animation fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            edtTimKiem.startAnimation(fadeInAnimation);

            // Đưa phần tử khác ra khỏi cảnh quan sát khi hiển thị EditText tìm kiếm (nếu cần)
//        searchImageView.setVisibility(View.GONE);
            searchImageView.setImageResource(R.drawable.ic_baseline_clear_24);
        } else {
            edtTimKiem.setVisibility(View.GONE); // ẩn EditText tìm kiếm
            edtTimKiem.setText("");
            // Tùy chỉnh hiệu ứng khi ẩn EditText tìm kiếm (nếu cần)
            Animation fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
            edtTimKiem.startAnimation(fadeInAnimation);

            // Đưa phần tử khác ra khỏi cảnh quan sát khi hiển thị EditText tìm kiếm (nếu cần)
//        searchImageView.setVisibility(View.GONE);
            searchImageView.setImageResource(R.drawable.ic_baseline_search_24);
        }

    }

    private void guiDuLieu(int position, String... str) {
        switch (position) {
            case 0: {
                fragment_sanPham.searchSanPham(str[0]);
                return;
            }
            case 1: {
                fragment_qLsanPham.searchSanPham(str[0]);
                return;
            }
            case 2: {
                return;
            }
            case 3: {
                return;
            }
            default: {
                return;
            }
        }
    }
}