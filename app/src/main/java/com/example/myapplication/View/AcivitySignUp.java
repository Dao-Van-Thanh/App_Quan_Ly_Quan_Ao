package com.example.myapplication.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.Presenter.NguoiDungPresanter;
import com.example.myapplication.R;
public class AcivitySignUp extends AppCompatActivity {

    EditText edtEmail, edtPass,edtFullname;
    Button btnLogin,btnSignUp;
    Context context;
    private NguoiDungPresanter presanter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_sign_up);

        anhXa();

        btnLogin.setOnClickListener((view) -> {
            startActivity(new Intent(context,ActivityLogin.class));
        });

        btnSignUp.setOnClickListener((view) -> {
            String fullname = edtFullname.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPass.getText().toString().trim();

            if (fullname.equals("") || email.equals("") || password.equals("")){
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
            }else if(!isValidEmail(email)){
                Toast.makeText(context, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            }else{
                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.setFullname(fullname);
                nguoiDung.setEmail(email);
                nguoiDung.setPassword(password);
                presanter.dangKy(nguoiDung);
            }
        });

    }

    private void anhXa() {
        context = this;
        edtEmail = findViewById(R.id.sign_up_edt_email);
        edtPass = findViewById(R.id.sign_up_edt_pass);
        edtFullname = findViewById(R.id.sign_up_edt_full_name);
        btnLogin = findViewById(R.id.sign_up_btn_login);
        btnSignUp = findViewById(R.id.sign_up_btn_sign_up);
        presanter = new NguoiDungPresanter(context);
    }
    public static boolean isValidEmail(String email) {
        // Sử dụng biểu thức chính quy từ Patterns để kiểm tra tính hợp lệ của email
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}