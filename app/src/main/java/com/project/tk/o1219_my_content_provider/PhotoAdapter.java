package com.project.tk.o1219_my_content_provider;


import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.tk.o1219_my_content_provider.databinding.ViewholderPhotoBinding;

import java.util.ArrayList;

/**
 * Created by conscious on 2017-12-19.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewholderPhoto> {

    private ArrayList<Uri> items;

    public PhotoAdapter(ArrayList<Uri> items) {
        this.items = items;
    }


    @Override
    public ViewholderPhoto onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewholderPhotoBinding b = ViewholderPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewholderPhoto(b.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewholderPhoto h, int p) {

        Uri u = items.get(p);
        h.b.setUri(u);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @BindingAdapter({"android:src"})
    public static void setImage(ImageView iv, Uri uri) {
        iv.setImageURI(uri);
    }


    class ViewholderPhoto extends RecyclerView.ViewHolder {
        private ViewholderPhotoBinding b;

        public ViewholderPhoto(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
        }
    }
}
