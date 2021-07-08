package com.demo.softexperttest;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerviewItemAdapter extends RecyclerView.Adapter<RecyclerviewItemAdapter.MyViewHolder> {

    private List<Cars> carsList;

    RecyclerviewItemAdapter(List<Cars> mCarsList){
        this.carsList = mCarsList;
    }

    public List<Cars> getCarsList() {
        return carsList;
    }

    public void setCarsList(List<Cars> carsList) {
        this.carsList = carsList;
    }

    @Override
    public RecyclerviewItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerviewItemAdapter.MyViewHolder holder, final int position) {
        final Cars car = carsList.get(position);
        holder.brand.setText(car.getBrand());
        holder.constructionYear.setText(car.getConstructionYear());
        holder.isUsed.setText(car.getIsUsed());
        MainActivity.imageLoader.displayImage(car.getImageUrl(), holder.carImage);

    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView brand,constructionYear,isUsed;
        public ImageView carImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            brand = itemView.findViewById(R.id.brand);
            constructionYear = itemView.findViewById(R.id.constructionYear);
            isUsed= itemView.findViewById(R.id.isUsed);
            carImage = itemView.findViewById(R.id.carImage);
        }
    }
}
