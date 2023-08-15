package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.SanPham;
import com.example.myapplication.Model.UrlApi;
import com.example.myapplication.R;

import java.util.List;


public class RecyclerGridViewAdapterSP extends RecyclerView.Adapter<RecyclerGridViewAdapterSP.ViewHolder> {
    private Context context;
    private List<SanPham> lsSP;


    public RecyclerGridViewAdapterSP(Context context) {
        this.context = context;
    }

    public void sendData(List<SanPham> lsSP){
        this.lsSP = lsSP;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(new UrlApi().url + "/Image/" + lsSP.get(position).getImage())
                .error(R.drawable.error_image_generic) // Resource ID của hình ảnh thông báo lỗi
                .into(holder.imageView);
        holder.tvTenSP.setText(lsSP.get(position).getName());
        holder.tvGiaSP.setText(lsSP.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        if (lsSP != null){
            return lsSP.size();
        }
        return 0;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvTenSP, tvGiaSP;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_san_pham_grid_imageView);
            tvTenSP = itemView.findViewById(R.id.item_san_pham_grid_textView_tensp);
            tvGiaSP = itemView.findViewById(R.id.item_san_pham_grid_textView_giasp);
        }
    }
}
