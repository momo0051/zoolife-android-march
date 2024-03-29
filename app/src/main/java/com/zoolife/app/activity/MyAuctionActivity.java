package com.zoolife.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.UserPost.DataItemNew;
import com.zoolife.app.ResponseModel.UserPost.UserAllPostResponseModelNew;
import com.zoolife.app.adapter.AdSliderAdapter;
import com.zoolife.app.adapter.CategoryAdapter;
import com.zoolife.app.adapter.MyPostAdapter;
import com.zoolife.app.adapter.NewSubCategoryAdapter;
import com.zoolife.app.adapter.SubCategoryAdapter;
import com.zoolife.app.models.HomeModel;
import com.zoolife.app.models.ImageData;
import com.zoolife.app.models.SubCategoryModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAuctionActivity extends AppBaseActivity {


    public static final int EDIT_POST_REQUEST_CODE = 1001;
    public int position = -1;

    ProgressBar progress_circular;

    MyPostAdapter homeAdapter;
    List<SubCategoryModel> subCategoryList;
    RecyclerView recyclerView, category_rv;

    List<HomeModel> dataList;

    TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //forceRTLIfSupported();

        setContentView(R.layout.activity_my_posts);


        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.my_auction));
        recyclerView = findViewById(R.id.home_data_rv);
        progress_circular = (ProgressBar) findViewById(R.id.progress_circular);

        getAllPost();
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            if (position != -1) {
//                dataList.remove(position);
//
//                homeAdapter.notifyItemRemoved(position);
//                homeAdapter.notifyItemRangeChanged(position, homeAdapter.getItemCount());
//
//                position = -1;

//            }
            Intent mIntent = getIntent();
            finish();
            startActivity(mIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void forceRTLIfSupported() {
        Objects.requireNonNull(this).getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }


    private void getAllPost() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<UserAllPostResponseModelNew> call = apiService.getAucationByUser(session.getUserId(),"auction");
        call.enqueue(new Callback<UserAllPostResponseModelNew>() {
            @Override
            public void onResponse(Call<UserAllPostResponseModelNew> call, Response<UserAllPostResponseModelNew> response) {
                UserAllPostResponseModelNew responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);

                    dataList = new ArrayList<>();

                    for (int i = 0; i < responseModel.getData().size(); i++) {
                        DataItemNew dataItem = responseModel.getData().get(i);
//                        System.out.println("Username-->" + dataItem.getUsername());
                        dataList.add(new HomeModel(dataItem.getItemTitle(), dataItem.getCreatedAt(), dataItem.getCity(), dataItem.getUsername(), dataItem.getImgUrl(), String.valueOf(dataItem.getId()), dataItem.getPriority()));

                    }


                    if (dataList.size() > 0) {
                        homeAdapter = new MyPostAdapter(MyAuctionActivity.this, dataList, progress_circular, session,1);
                        recyclerView.setAdapter(homeAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }

                } else {
                    // infoDialog("Server Error.");
                    progress_circular.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<UserAllPostResponseModelNew> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }
}