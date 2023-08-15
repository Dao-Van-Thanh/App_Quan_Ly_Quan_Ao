package com.example.myapplication.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.InterFace.InterFaceLoading;
import com.example.myapplication.InterFace.InterfaceNguoiDung;
import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.Presenter.NguoiDungPresanter;
import com.example.myapplication.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class ActivityLogin extends AppCompatActivity {

    EditText edtEmail, edtPass;
    Button btnLogin, btnSignUp;
    Context context;
    private NguoiDungPresanter nguoiDungPresanter;
    private AVLoadingIndicatorView avlLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        checkAutoLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().isEmpty() && !edtPass.getText().toString().isEmpty()) {
                    login();
                } else {
                    Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, AcivitySignUp.class));
            }
        });


    }

    private void anhXa() {
        edtEmail = findViewById(R.id.login_edt_email);
        edtPass = findViewById(R.id.login_edt_pass);
        btnLogin = findViewById(R.id.login_btn_login);
        btnSignUp = findViewById(R.id.login_btn_sign_up);
        context = this;
        nguoiDungPresanter = new NguoiDungPresanter(context);
        avlLoading = findViewById(R.id.avl_loading);
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        nguoiDungPresanter.checkLogin(email, password, new InterfaceNguoiDung() {
            @Override
            public void getDataSanPham(List<NguoiDung> list) {
            }

            @Override
            public void getNguoiDungLogin(NguoiDung nguoiDung) {
                if (nguoiDung != null) {

                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    luuTaiKhoan(nguoiDung.getEmail(), nguoiDung.getPassword(), true);

                    Intent intent = new Intent(context, ActivityDisplay.class);
                    intent.putExtra("id",nguoiDung.getId());
                    intent.putExtra("image", nguoiDung.getImage());
                    intent.putExtra("fullname", nguoiDung.getFullname());
                    intent.putExtra("email", nguoiDung.getEmail());
                    intent.putExtra("password", nguoiDung.getPassword());
                    intent.putExtra("admin", nguoiDung.getAdmin());
                    startActivity(intent);
                }
            }

            @Override
            public void getStatus(int status) {
                if (status >= 400) {
                    Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new InterFaceLoading() {
            @Override
            public void isLoading(boolean loading) {
                if (loading) {
                    avlLoading.show();
                } else {
                    avlLoading.hide();
                }
            }
        });
    }

    private void luuTaiKhoan(String username, String password, boolean check) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("check", check);
        editor.apply();
    }

    private void checkAutoLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

        if (!username.isEmpty() && !password.isEmpty()) {
            edtEmail.setText(username);
            edtPass.setText(password);
        }
    }

}