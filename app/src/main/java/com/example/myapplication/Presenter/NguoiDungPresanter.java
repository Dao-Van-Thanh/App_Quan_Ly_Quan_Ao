package com.example.myapplication.Presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.InterFace.InterFaceDangKyNguoiDung;
import com.example.myapplication.InterFace.InterFaceLoading;
import com.example.myapplication.InterFace.InterfaceNguoiDung;
import com.example.myapplication.Model.NguoiDung;
import com.example.myapplication.Model.UrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NguoiDungPresanter {
    private Context context;
    public String url = new UrlApi().url + "api/datauser";
    public List<NguoiDung> list = new ArrayList<>();

    public NguoiDungPresanter(Context context) {
        this.context = context;
    }

    public void getData(InterfaceNguoiDung interfaceNguoiDung) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                try {
                                    JSONObject nguoiDungs = dataArray.getJSONObject(i);
                                    NguoiDung nguoiDung = new NguoiDung();
                                    nguoiDung.setId(nguoiDungs.getString("_id"));
                                    nguoiDung.setImage(nguoiDungs.getString("image"));
                                    nguoiDung.setFullname(nguoiDungs.getString("fullname"));
                                    nguoiDung.setEmail(nguoiDungs.getString("email"));
                                    nguoiDung.setPassword(nguoiDungs.getString("password"));
                                    if (nguoiDungs.has("admin")) {
                                        nguoiDung.setAdmin(nguoiDungs.getString("admin"));
                                    } else {
                                        nguoiDung.setAdmin(""); // Hoặc bạn có thể gán giá trị mặc định nếu không có thuộc tính "admin"
                                    }
                                    list.add(nguoiDung);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            interfaceNguoiDung.getDataSanPham(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        interfaceNguoiDung.getStatus(error.networkResponse.statusCode);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void checkLogin(String email, String password, InterfaceNguoiDung interfaceNguoiDung, InterFaceLoading interFaceLoading) {
        String url_checkLogin = url + "/login"; // Thay thế bằng URL API xác thực đăng nhập của bạn

        try {
            // Tạo một JSONObject chứa dữ liệu đăng nhập
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            interFaceLoading.isLoading(true);
            // Tạo một StringRequest với method là POST và chứa JSONObject dữ liệu
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_checkLogin,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Xử lý phản hồi từ server sau khi gửi thành công
                            // response chứa kết quả từ server
                            try {
                                JSONObject object = new JSONObject(response);
                                NguoiDung nguoiDung = new NguoiDung();
                                nguoiDung.setId(object.getString("_id"));
                                nguoiDung.setImage(object.getString("image"));
                                nguoiDung.setEmail(object.getString("email"));
                                nguoiDung.setPassword(object.getString("password"));
                                nguoiDung.setFullname(object.getString("fullname"));
                                if (object.has("admin")) {
                                    nguoiDung.setAdmin(object.getString("admin"));
                                }
                                interfaceNguoiDung.getNguoiDungLogin(nguoiDung);
                                interFaceLoading.isLoading(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi khi gửi yêu cầu lên server
                            System.out.println(error.getMessage());
                            interfaceNguoiDung.getStatus(error.networkResponse.statusCode);
                        }
                    }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    // Chuyển JSONObject thành byte array để gửi lên server
                    try {
                        return jsonObject.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public String getBodyContentType() {
                    // Thiết lập kiểu dữ liệu cho yêu cầu POST
                    return "application/json";
                }
            };
            // Thêm yêu cầu vào RequestQueue để thực hiện
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void dangKy(NguoiDung nguoiDung) {
        try {
            JSONObject object = new JSONObject();
            object.put("fullname", nguoiDung.getFullname());
            object.put("email", nguoiDung.getEmail());
            object.put("password", nguoiDung.getPassword());

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(context, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.networkResponse.statusCode);
                        }
                    }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    // Chuyển JSONObject thành byte array để gửi lên server
                    try {
                        return object.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public String getBodyContentType() {
                    // Thiết lập kiểu dữ liệu cho yêu cầu POST
                    return "application/json";
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    // Tạo một map params để gửi lên server với key "user"
                    Map<String, String> params = new HashMap<>();
                    JSONObject userObject = new JSONObject();
                    try {
                        userObject.put("id", nguoiDung.getId());
                        userObject.put("image", nguoiDung.getImage());
                        userObject.put("fullname", nguoiDung.getFullname());
                        userObject.put("email", nguoiDung.getEmail());
                        userObject.put("password", nguoiDung.getPassword());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    params.put("user", userObject.toString());
                    return params;
                }
            };

            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadImageToServer(NguoiDung nguoiDung, Uri imageUri) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Xử lý phản hồi từ server nếu cần thiết
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

        // Thêm thông tin người dùng vào phần params của yêu cầu
        try {
            System.out.println(nguoiDung);
            JSONObject userObject = new JSONObject();
            userObject.put("id", nguoiDung.getId());
            userObject.put("fullname", nguoiDung.getFullname());
            userObject.put("email", nguoiDung.getEmail());
            userObject.put("password", nguoiDung.getPassword());
            // Gửi thông tin người dùng trong phần params của yêu cầu
            multipartRequest.addParam("user", userObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Thêm file ảnh vào phần body của yêu cầu
        try {
            // Mở luồng đọc tệp tin từ Uri
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Đọc dữ liệu từ luồng
            byte[] fileData = readFile(inputStream);
            multipartRequest.addMultipartData("image", new VolleyMultipartRequest.VolleyMultipartData() {
                @Override
                public String getFileName() {
                    return getFileNameFromUri(imageUri); // Sử dụng phương thức đã viết để lấy tên file từ Uri
                }

                @Override
                public String getMimeType() {
                    return context.getContentResolver().getType(imageUri);
                }

                @Override
                public byte[] getData() {
                    return fileData;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(multipartRequest);
    }

    // Phương thức lấy tên file từ Uri
    private String getFileNameFromUri(Uri imageUri) {
        String fileName = null;
        if (imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = context.getContentResolver().query(imageUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        if (fileName == null) {
            // Nếu không lấy được tên file từ Uri, sử dụng một tên mặc định
            fileName = "image.jpg";
        }
        return fileName;
    }

    private byte[] readFile(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
