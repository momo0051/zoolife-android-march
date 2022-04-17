package com.zoolife.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zoolife.app.R;
import com.zoolife.app.Session;
import com.zoolife.app.activity.AuctionPostDetailsActivity;
import com.zoolife.app.fragments.HomeFragment;
import com.zoolife.app.models.AuctionModel;
import com.zoolife.app.models.HomeModel;
import com.zoolife.app.utility.TimeShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AuctionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<AuctionModel.MData> data;
    AuctionModel.MData current;
    private final Session session;
    HomeFragment fragment;
    private static final String TAG = "AuctionAdapter";
    private String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    final int VIEW_TYPE_NORMAL = 0;
    final int VIEW_TYPE_AD_MOB = 1;

    public AuctionAdapter(Context context, ArrayList<AuctionModel.MData> auctionArray, Session session) {
        this.context = context;
        this.data = auctionArray;
        this.session = session;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View searchResultView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_auction, parent, false);
            return new MyViewHolder(searchResultView);
        }
        View searchResultView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admob, parent, false);
        return new MyAddViewHolder(searchResultView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder hldr, int position) {
        current = data.get(position);
        if (current.getViewType() == 0) {//for normal view

            MyViewHolder holder = (MyViewHolder) hldr;

            if (current.getPriority().equals("0") || current.getPriority() == null) {
                holder.itemImage.setBorderColor(context.getResources().getColor(R.color.transparent));
            } else {
                holder.itemImage.setBorderColor(context.getResources().getColor(R.color.yellow));
            }
            holder.itemTitle.setText(current.getItemTitle());
            holder.item_city.setText(current.getCity());
            TimeShow timeShow = new TimeShow();
            holder.status.setText(timeShow.convertDate(current.getAuction_expiry_time(), context));
            holder.item_latest_Bid.setText(current.getLatest_bid());

            if (current.getVideoUrl() != null && !current.getVideoUrl().isEmpty()) {
                holder.item_ivPlay.setVisibility(View.VISIBLE);
            } else {
                holder.item_ivPlay.setVisibility(View.GONE);
            }


            holder.item_user_postedby_layout.setVisibility(View.VISIBLE);

            final RequestOptions requestOptions = new RequestOptions()
                    .transforms(new CenterCrop(), new RoundedCorners(16));


            Glide.with(context)
                    .load(R.drawable.placeholder)
                    .centerCrop()
                    .apply(requestOptions)
                    .into(holder.itemImage);
            if (!current.getImgUrl().equals("")) {
                Glide.with(context)
                        .load(current.getImgUrl())
                        .centerCrop()
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.itemImage.post(() -> Glide.with(context)
                                        .load(R.drawable.placeholder)
                                        .centerCrop()
                                        .apply(requestOptions)
                                        .into(holder.itemImage));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(holder.itemImage);
            }

            if (!current.getVideoUrl().isEmpty()) {
                Glide.with(context)
                        .load(current.getVideoUrl())
                        .into(holder.itemImage);
            }

            countDownStart(current.getAuction_expiry_time(), holder.status);

        } else if (current.getViewType() == 1 && (position == 6)) {

            MyAddViewHolder hdl= (MyAddViewHolder) hldr;
            hdl.templateView.setVisibility(View.VISIBLE);
            AdLoader.Builder builder = new AdLoader.Builder(
                    context, context.getString(R.string.ad_unit_id));
            builder.forNativeAd(unifiedNativeAd -> hdl.templateView.setNativeAd(unifiedNativeAd));
            final AdLoader adLoader = builder.build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
        else if (current.getViewType()==1){
            Log.e("Show", "Dont show anything");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getViewType() == 1 && (position == 6)) {
            return VIEW_TYPE_AD_MOB;
        }
        else if (data.get(position).getViewType() == 1) {
            return VIEW_TYPE_AD_MOB;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setEvenData() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoundedImageView itemImage;
        ImageView itemFavImage, item_ivPlay;
        TextView itemTitle, item_city, status, item_latest_Bid;
        LinearLayout singleCLick;
        LinearLayout item_user_postedby_layout;
        TemplateView templateView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            templateView = itemView.findViewById(R.id.templateView);
            singleCLick = itemView.findViewById(R.id.single_click);
            itemImage = itemView.findViewById(R.id.item_image);
            itemFavImage = itemView.findViewById(R.id.item_fav_image);
            item_ivPlay = itemView.findViewById(R.id.item_ivPlay);
            itemTitle = itemView.findViewById(R.id.item_title);
            item_city = itemView.findViewById(R.id.item_city);
            status = itemView.findViewById(R.id.status);
            item_user_postedby_layout = itemView.findViewById(R.id.item_user_postedby_layout);
            item_latest_Bid = itemView.findViewById(R.id.item_latest_Bid);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (context != null) {
                AuctionModel.MData model = data.get(getAdapterPosition());
                Intent intent = new Intent(context, AuctionPostDetailsActivity.class);
                intent.putExtra("id", String.valueOf(model.getId()));
                intent.putExtra("from", "Home");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        }
    }

    public class MyAddViewHolder extends RecyclerView.ViewHolder {

        TemplateView templateView;

        public MyAddViewHolder(@NonNull View itemView) {
            super(itemView);
            templateView = itemView.findViewById(R.id.templateView);
        }
    }

    public void SetData(ArrayList<AuctionModel.MData> data) {
        this.data = data;
//        this.data.addAll(data);
        notifyDataSetChanged();
    }


    private void countDownStart(String EVENT_DATE_TIME, TextView tv) {
//          String EVENT_DATE_TIME = "2021-12-31 10:30:00";
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;

//                        String time=Days+" days "+Hours+" h "+Minutes+ " mnt "+Seconds+" seconds";
                        String time = String.format(context.getString(R.string.days), Days) + String.format(context.getString(R.string.hrs), Hours) +
                                String.format(context.getString(R.string.mnt), Minutes) + String.format(context.getString(R.string.sec), Seconds);
                        tv.setText(time);
//                        Log.e(TAG, "run: timer" + time);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

}
