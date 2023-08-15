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
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

public class RecyclerViewAdapterQLSP extends RecyclerView.Adapter<RecyclerViewAdapterQLSP.ViewHolder> {

    private Context context;
    private List<SanPham> lsSP;
    public RecyclerViewAdapterQLSP(Context context) {
        this.context = context;

    }

    public void SendData(List<SanPham> lsSP) {
        this.lsSP = lsSP;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_qlsp_folding_cell, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(lsSP.get(position).getName());
        holder.txtName2.setText(lsSP.get(position).getName());
        holder.txtType.setText(lsSP.get(position).getType());
        holder.txtColor.setText(lsSP.get(position).getColor());
        holder.txtPrice.setText(lsSP.get(position).getPrice());
        holder.txtPrice2.setText(lsSP.get(position).getPrice());

        Glide.with(context)
                .load(new UrlApi().url + "/Image/" + lsSP.get(position).getImage())
                .error(R.drawable.error_image_generic) // Resource ID của hình ảnh thông báo lỗi
                .into(holder.imageView);
        Glide.with(context)
                .load(new UrlApi().url + "/Image/" +lsSP.get(position).getImage())
                .error(R.drawable.error_image_generic) // Resource ID của hình ảnh thông báo lỗi
                .into(holder.imageView2);

        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (lsSP != null) {
            return lsSP.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtPrice, txtType, txtColor, txtName2, txtPrice2;
        private ImageView imageView, imageView2;
        private FoldingCell foldingCell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.item_qlsp_txt_name);
            txtPrice = itemView.findViewById(R.id.item_qlsp_txt_price);
            txtType = itemView.findViewById(R.id.item_qlsp_txt_type);
            txtColor = itemView.findViewById(R.id.item_qlsp_txt_color);
            txtName2 = itemView.findViewById(R.id.item_qlsp_txt_name2);
            txtPrice2 = itemView.findViewById(R.id.item_qlsp_txt_price2);
            imageView = itemView.findViewById(R.id.item_qlsp_imageview);
            imageView2 = itemView.findViewById(R.id.item_qlsp_imageview2);
            foldingCell = itemView.findViewById(R.id.folding_cell);
        }
    }
}
