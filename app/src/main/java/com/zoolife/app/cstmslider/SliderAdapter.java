package com.zoolife.app.cstmslider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;
import com.zoolife.app.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    Context context;
    ArrayList<DtoImageVideo> list;
    OnClickListener listener;
    SimpleExoPlayer player;
    private static final String TAG = "slideradapter";

    public SliderAdapter(Context context, ArrayList<DtoImageVideo> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.player = new SimpleExoPlayer.Builder(context).build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_slider, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DtoImageVideo obj = list.get(position);
         if (!obj.isImage()) {
            holder.playerView.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            playAudio(obj.getUri(), holder.playerView);
        } else {
            holder.playerView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);


//            Uri uri = Uri.parse(obj.getUri());
            holder.imageView.setImageURI(obj.getUri());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLikeClick(position);
            }
        });
        holder.playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLikeClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        SimpleDraweeView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }

    public interface OnClickListener {
        void onLikeClick(int position);
    }

    void playAudio(String audioUri, PlayerView exoPlayer) {
        stopAndRelease();
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        exoPlayer.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();


    }

    void stopAndRelease() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }
    }
}
