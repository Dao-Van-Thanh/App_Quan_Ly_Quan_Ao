package com.example.myapplication.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.Model.UrlApi;
import com.example.myapplication.Presenter.NguoiDungPresanter;
import com.example.myapplication.R;

import java.io.File;


public class ActivityThongTin extends AppCompatActivity {
    ImageView imageView;
    EditText edtFullName, edtEmail;
    Button btnSuaImage, btnSuaFullName, btnSuaEmail, btnQuayLai, btnUpdate;
    Context context;
    NguoiDung nguoiDung;
    private ActivityResultLauncher<String> galleryLauncher;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);
        anhXa();

        // đăng ký hành động lấy nội dung(ảnh) trong điện thoại
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    imageUri = result;
                    Glide.with(context)
                            .load(imageUri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // Không lưu vào bộ nhớ cache để đảm bảo cập nhật ảnh mới nhất
                            .skipMemoryCache(true) // Bỏ qua bộ nhớ cache
                            .error(R.drawable.error_image_generic) // Hình ảnh thông báo lỗi nếu không thể tải ảnh
                            .into(imageView);
                }
            }
        });

        layThongTin();
        suKienOnclick();


    }

    private void layThongTin() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            nguoiDung = (NguoiDung) bundle.getSerializable("NguoiDung");
            String urlApi = new UrlApi().url + "/Image/" + nguoiDung.getImage();
            Glide.with(context)
                    .load(urlApi)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Không lưu vào bộ nhớ cache để đảm bảo cập nhật ảnh mới nhất
                    .skipMemoryCache(true) // Bỏ qua bộ nhớ cache
                    .error(R.drawable.error_image_generic) // Resource ID của hình ảnh thông báo lỗi
                    .into(imageView);
            edtFullName.setText(nguoiDung.getFullname());
            edtEmail.setText(nguoiDung.getEmail());
        } else {
            Toast.makeText(context, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private void suKienOnclick() {
        btnQuayLai.setOnClickListener((view) -> {
            super.onBackPressed();
        });

        btnSuaFullName.setOnClickListener((view) -> {
            edtFullName.setEnabled(true);
            edtFullName.setBackgroundResource(R.drawable.custom_edit_text);
            hienBtnUpdate();
        });

        btnSuaEmail.setOnClickListener((view) -> {
            edtEmail.setEnabled(true);
            edtEmail.setBackgroundResource(R.drawable.custom_edit_text);
            hienBtnUpdate();
        });

        btnSuaImage.setOnClickListener((view) -> {
            hienBtnUpdate();
            openGallery();
//            File file = new File(imageUri.getPath());
        });

        btnUpdate.setOnClickListener((view) -> {
            nguoiDung.setFullname(edtFullName.getText().toString().trim());
            nguoiDung.setEmail(edtEmail.getText().toString().trim());

            NguoiDungPresanter presanter = new NguoiDungPresanter(context);
            presanter.uploadImageToServer(nguoiDung,imageUri);
        });
    }

    private void anhXa() {
        imageView = findViewById(R.id.activity_thong_tin_image_view);
        edtFullName = findViewById(R.id.activity_thong_tin_edt_fullname);
        edtEmail = findViewById(R.id.activity_thong_tin_edt_email);
        btnSuaImage = findViewById(R.id.activity_thong_tin_btn_sua_image);
        btnSuaFullName = findViewById(R.id.activity_thong_tin_btn_sua_fullname);
        btnSuaEmail = findViewById(R.id.activity_thong_tin_btn_sua_email);
        btnQuayLai = findViewById(R.id.activity_thong_tin_btn_quay_lai);
        btnUpdate = findViewById(R.id.activity_thong_tin_btn_sua_update);
        context = this;
    }

    private void hienBtnUpdate() {
        btnUpdate.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        btnUpdate.setAnimation(animation);
    }

    // mở thư viện ảnh, sử dụng galleryLauncher đã đăng ký trước đó để lấy ảnh
    private void openGallery() {
        galleryLauncher.launch("image/*");
    }
}