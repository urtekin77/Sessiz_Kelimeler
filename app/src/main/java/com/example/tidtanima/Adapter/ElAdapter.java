package com.example.tidtanima.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;

import com.example.tidtanima.Data.el;
import com.example.tidtanima.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ElAdapter extends BaseAdapter {

    private Context context;
    private List<el> elList;
    public ElAdapter(Context context, List<el> elList){
        this.elList = elList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return elList != null ? elList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return elList != null ? elList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return elList != null ? position : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = LayoutInflater.from(context).inflate(R.layout.spinner_el_view, parent,false);
       ImageView imageView = view.findViewById(R.id.elImageView);
       String E_ID ;
        Picasso.get()
                .load(elList.get(position).getE_hareketi())
                .placeholder(R.drawable.install) // Yükleme sırasında gösterilecek yer tutucu
                .error(R.drawable.error) // Hata durumunda gösterilecek yer tutucu
                .into(imageView);


        return view;
    }
}
