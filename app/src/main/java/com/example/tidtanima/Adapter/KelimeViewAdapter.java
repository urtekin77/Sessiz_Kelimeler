package com.example.tidtanima.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class KelimeViewAdapter extends RecyclerView.Adapter<KelimeViewAdapter.KelimeViewHolder> {

    List<isaret> isarets;

    public KelimeViewAdapter(List<isaret> isarets) {
        this.isarets = isarets;
    }

    @NonNull
    @Override
    public KelimeViewAdapter.KelimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kelimler_view, parent, false);
        return new KelimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KelimeViewHolder holder, int position) {
        isaret isaret = isarets.get(position);
        holder.anlam.setText(isaret.getI_anlam());
        holder.kelime.setText(isaret.getI_ad());
        holder.I_ID = isaret.getI_ID();

        String resimUrl = isaret.getI_resim();
        String videoUrl = isaret.getI_video();

        if (resimUrl != null && !resimUrl.isEmpty()) {
            Log.d("KelimeViewAdapter", "Displaying image from URL: " + resimUrl);
            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(resimUrl).into(holder.imageView);
        } else if (videoUrl != null && !videoUrl.isEmpty()) {
            Log.d("KelimeViewAdapter", "Attempting to load video from URL: " + videoUrl);
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);

            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl);
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    holder.setVideoUri(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FirebaseStorage", "Video download failed: " + e.getMessage());
                }
            });

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.videoView.isPlaying()) {
                        holder.videoView.pause();
                    } else {
                        holder.videoView.start();
                    }
                }
            });
        } else {
            Log.e("KelimeViewAdapter", "Both resimUrl and videoUrl are null or empty.");
        }
    }

    @Override
    public int getItemCount() {
        return isarets.size();
    }

    public class KelimeViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageView imageView;
        TextView kelime, anlam;
        String I_ID, E_ID, Y_ID;

        public KelimeViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoViewA);
            imageView = itemView.findViewById(R.id.resimViewA);
            kelime = itemView.findViewById(R.id.kelime);
            anlam = itemView.findViewById(R.id.anlam);
        }

        public void setVideoUri(Uri uri) {
            videoView.setVideoURI(uri);
            videoView.seekTo(1); // Show the first frame
        }
    }
}
