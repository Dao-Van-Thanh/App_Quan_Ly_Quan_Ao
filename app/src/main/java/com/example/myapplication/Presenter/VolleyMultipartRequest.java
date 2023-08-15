package com.example.myapplication.Presenter;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<String> {
    private final String boundary = "volleyMultipartBoundary";
    private final String lineEnd = "\r\n";
    private Map<String, String> headers;
    private Response.Listener<String> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> params;
    private Map<String, VolleyMultipartData> multipartData;

    public VolleyMultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mErrorListener = errorListener;
        headers = new HashMap<>();
        params = new HashMap<>();
        multipartData = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public void addMultipartData(String key, VolleyMultipartData data) {
        multipartData.put(key, data);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                bos.write(("--" + boundary + lineEnd).getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd).getBytes());
                bos.write((lineEnd + entry.getValue() + lineEnd).getBytes());
            }

            for (Map.Entry<String, VolleyMultipartData> entry : multipartData.entrySet()) {
                VolleyMultipartData data = entry.getValue();
                bos.write(("--" + boundary + lineEnd).getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + data.getFileName() + "\"" + lineEnd).getBytes());
                bos.write(("Content-Type: " + data.getMimeType() + lineEnd).getBytes());
                bos.write((lineEnd).getBytes());
                bos.write(data.getData());
                bos.write((lineEnd).getBytes());
            }
            bos.write(("--" + boundary + "--" + lineEnd).getBytes());

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    public interface VolleyMultipartData {
        String getFileName();
        String getMimeType();
        byte[] getData();
    }
}
