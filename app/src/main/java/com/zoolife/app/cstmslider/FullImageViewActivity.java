package com.zoolife.app.cstmslider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.zoolife.app.R;
import com.zoolife.app.activity.AuctionPostDetailsActivity;

import java.util.ArrayList;

public class FullImageViewActivity extends AppCompatActivity implements
        SliderAdapter.OnClickListener {
    RecyclerView rv;
    private ArrayList<DtoImageVideo> listOfImage = new ArrayList<>();
    private SliderAdapter adaptors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);
        rv = findViewById(R.id.rvPhotos);
        adaptors = new SliderAdapter(FullImageViewActivity.this, listOfImage, this);
        String position = getIntent().getStringExtra("position");
        AuctionPostDetailsActivity.list.observe(this, new Observer<ArrayList<DtoImageVideo>>() {
            @Override
            public void onChanged(ArrayList<DtoImageVideo> dtoImageVideos) {
                listOfImage.addAll(dtoImageVideos);
                adaptors.notifyDataSetChanged();
                rv.setLayoutManager(new LinearLayoutManager(FullImageViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
                rv.setAdapter(adaptors);
                rv.scrollToPosition(Integer.parseInt(position));

                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(rv);
            }
        });

    }

    @Override
    public void onLikeClick(int position) {

    }
}
