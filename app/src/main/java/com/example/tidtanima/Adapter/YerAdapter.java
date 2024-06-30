package com.example.tidtanima.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tidtanima.Data.yer;
import com.example.tidtanima.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YerAdapter extends BaseAdapter {
    private Context context;
    private List<yer> yerList;
    public YerAdapter(Context context, List<yer> yerList){
        this.yerList = yerList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return yerList != null ? yerList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return yerList != null ? position : 0;
    }

    @Override
    public long getItemId(int position) {
        return yerList != null ? position : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_yer_view, parent,false);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.adView);
        textView.setText(yerList.get(position).getY_ad());
        Picasso.get()
                .load(yerList.get(position).getY_bolge())
                .placeholder(R.drawable.install) // Yükleme sırasında gösterilecek yer tutucu
                .error(R.drawable.error) // Hata durumunda gösterilecek yer tutucu
                .into(imageView);



        return view;
    }
}
