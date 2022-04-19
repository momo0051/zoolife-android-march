package com.zoolife.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.custom.sliderimage.logic.SliderImage;
import com.squareup.picasso.Picasso;
import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.AddComment.AddCommentResponseModel;
import com.zoolife.app.ResponseModel.AddPost.AddPostResponseModel;
import com.zoolife.app.ResponseModel.FavModel.FavResponseModel;
import com.zoolife.app.ResponseModel.GetPost.GetPostResponseModel;
import com.zoolife.app.ResponseModel.GetPost.Image;
import com.zoolife.app.ResponseModel.GetUserProfile.GetUserProfileResponseModel;
import com.zoolife.app.ResponseModel.NoDataResponseModel;
import com.zoolife.app.ResponseModel.ShowComment.DataItem;
import com.zoolife.app.ResponseModel.ShowComment.ViewCommentsResponseModel;
import com.zoolife.app.adapter.CommentsAdapter;
import com.zoolife.app.adapter.ImageLoaderAdapter;
import com.zoolife.app.adapter.ImageLoaderAdapterRa;
import com.zoolife.app.cstmslider.DtoImageVideo;
import com.zoolife.app.cstmslider.FullImageViewActivity;
import com.zoolife.app.cstmslider.LinePagerIndicatorDecoration;
import com.zoolife.app.cstmslider.SliderAdapter;
import com.zoolife.app.models.CommentModel;
import com.zoolife.app.models.ImageModel;
import com.zoolife.app.models.RelatedAdModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;
import com.zoolife.app.utility.ItemOffsetDecoration;
import com.zoolife.app.utility.TimeShow;
import com.zoolife.app.utility.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDetailsActivity extends AppBaseActivity implements SliderAdapter.OnClickListener {
    EditText commentET;
    ProgressBar progress_circular;
    CommentsAdapter commentsAdapter;
    RecyclerView commentRV;
    String add_id = "", from = "";
    TextView descriptionTv, titleTv, idUser, date, city; /*,country;*/
    ImageView adImage;
    RecyclerView more_imagesRV, more_imagesRA;
    ImageLoaderAdapter imageLoaderAdapter;
    ImageLoaderAdapterRa imageLoaderAdapterRA;
    LinearLayout CMLayout, EDLayout, showall_ll;
    ImageView likeBtn, icLike;
    Button fullComments;
    ImageView shareBtn;
    String sharUrl = "";
    private ArrayList<DtoImageVideo> listImageView = new ArrayList<>();

    private TextView messageBtn, tvCount;

    private GetPostResponseModel responseModel;

    boolean isLike = false;
    boolean isLikeAll = false;
    boolean isReported = false;

    private String adImageUrl;
    private ImageView ivCall, ivWhatsapp, ivChat, ivReport;
    private LinearLayout llComment;
    private static Retrofit retrofit = null;
    private String userName;

    private View.OnClickListener messageClickListener = v -> {
        if (session.isLogin()) {
            if (responseModel != null) {
                progress_circular.setVisibility(View.VISIBLE);
//                String email = responseModel.getData().getEmail();
                String username = responseModel.getData().getPhone();
//                String username = responseModel.getData().getPhone().equals(session.getPhone()) ? session.getPhone() : responseModel.getData().getPhone();

                ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
                Call<GetUserProfileResponseModel> call = apiService.getUserProfile(responseModel.getData().getId());
                call.enqueue(new Callback<GetUserProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<GetUserProfileResponseModel> call, Response<GetUserProfileResponseModel> response) {
                        progress_circular.setVisibility(View.GONE);
                        GetUserProfileResponseModel responseModel = response.body();
                        if (response.isSuccessful() && responseModel.getData() != null) {
                            Intent chatIntent = new Intent(AddDetailsActivity.this, ChatActivity.class);
                            chatIntent.putExtra(ChatActivity.AD_ID, AddDetailsActivity.this.responseModel.getData().getId());
                            chatIntent.putExtra(ChatActivity.AD_TITLE, AddDetailsActivity.this.responseModel.getData().getItemTitle());
                            chatIntent.putExtra(ChatActivity.AD_CREATED_USER, AddDetailsActivity.this.responseModel.getData().getUsername());
                            chatIntent.putExtra(ChatActivity.USER_OBJ, responseModel);
                            chatIntent.putExtra(ChatActivity.GROUP_ID, "");
                            startActivity(chatIntent);
                            progress_circular.setVisibility(View.GONE);

                        } else {
                            Log.d("AddDetailsActivity", "Server Error.");
                            progress_circular.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUserProfileResponseModel> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        progress_circular.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            Toast.makeText(AddDetailsActivity.this, "You have to login first to chat", Toast.LENGTH_SHORT).show();
        }
    };
    private TextView liketxt, favtxt, sharetxt, reporttxt;
    private TextView age, passport, vaccine, postid;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //forceRTLIfSupported();
        add_id = getIntent().getStringExtra("id");

//        Log.i("showwhhatsapp", "getallpost: setting " + data.get(getAdapterPosition()).getShowWhatsapp());
        Log.i("asuydfgwrge", ":" + add_id);

        setContentView(R.layout.activity_add_details);

        CMLayout = findViewById(R.id.CMLayout);
        EDLayout = findViewById(R.id.EDLayout);
        showall_ll = findViewById(R.id.showall_ll);
        likeBtn = findViewById(R.id.likeBtn);
        icLike = findViewById(R.id.ic_like);
        liketxt = findViewById(R.id.liketxt);
        favtxt = findViewById(R.id.favtxt);
        reporttxt = findViewById(R.id.reporttxt);
        sharetxt = findViewById(R.id.sharetxt);

        ivCall = findViewById(R.id.iv_call);
        ivWhatsapp = findViewById(R.id.iv_whatsapp);
        ivChat = findViewById(R.id.iv_chat);

        ivReport = findViewById(R.id.iv_report);
        tvCount = findViewById(R.id.tv_count);
        llComment = findViewById(R.id.ll_comment);

        fullComments = findViewById(R.id.full_comment);
        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharUrl.isEmpty()) {
                    Utils.shareApp(AddDetailsActivity.this, sharUrl);
                }
            }
        });
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseModel.getData().getPhone() != null && !responseModel.getData().getPhone().isEmpty()) {
                    openWhatsApp(responseModel.getData().getPhone(), "");
                } else {
                    Toast.makeText(AddDetailsActivity.this, "Phone not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fullComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullCommentActivity.class);
                intent.putExtra("id", add_id);
                intent.putExtra("username", userName);
                startActivity(intent);
            }
        });


        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + responseModel.getData().getPhone()));
                startActivity(intent);
            }
        });


        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserProfileApi();

            }
        });
        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reportPost("Reported");
            }
        });


        commentET = findViewById(R.id.commentET);
        commentRV = findViewById(R.id.commentRV);
        descriptionTv = findViewById(R.id.descriptionTv);
        titleTv = findViewById(R.id.titleTv);
        progress_circular = findViewById(R.id.progress_circular);
        adImage = findViewById(R.id.addImage);
        idUser = findViewById(R.id.idUser);
        date = findViewById(R.id.date);
        city = findViewById(R.id.city);
//        country = findViewById(R.id.country);
        more_imagesRV = findViewById(R.id.more_imagesRV);
        more_imagesRA = findViewById(R.id.more_imagesRA);
        messageBtn = findViewById(R.id.messageBtn);
        age = findViewById(R.id.age);
        passport = findViewById(R.id.passport);
        postid = findViewById(R.id.postid);
        vaccine = findViewById(R.id.vaccine);
        rv = findViewById(R.id.rvSlider);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(linearLayoutManager);
        linearLayoutManager.findFirstVisibleItemPosition();
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(rv);


        from = getIntent().getStringExtra("from");

        if (from != null) {
            if (from.equalsIgnoreCase("home")) {
//                CMLayout.setVisibility(View.VISIBLE);
                CMLayout.setVisibility(View.GONE);
                EDLayout.setVisibility(View.GONE);
            }
        } else {
            CMLayout.setVisibility(View.GONE);
            EDLayout.setVisibility(View.VISIBLE);

        }


        fetchAd("" + add_id);


        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!session.isLogin()) {
                    Toast.makeText(AddDetailsActivity.this, "You have to login first to like", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isLike) {
                    isLike = false;
                    doFavAdd(0);
                    likeBtn.setImageResource(R.drawable.ic_heart);
                } else {
                    isLike = true;
                    doFavAdd(1);
                    likeBtn.setImageResource(R.drawable.ic_heart_filled);
                }
            }
        });

        icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                icLike.setClickable(false);
                icLike.setEnabled(false);
                if (!session.isLogin()) {
                    Toast.makeText(AddDetailsActivity.this, "You have to login first to like", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isLikeAll) {
                    isLikeAll = false;
                    doLikeAdd();
                    icLike.setImageResource(R.drawable.ic_like_unchecked);
                } else {
                    isLikeAll = true;
                    doLikeAdd();
                    icLike.setImageResource(R.drawable.ic_like);
                }
            }
        });


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!session.isLogin()) {
                    Toast.makeText(AddDetailsActivity.this, "You have to login first to comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!commentET.getText().toString().isEmpty()) {
                    addComment(commentET.getText().toString());
                } else {
                    commentET.setError("الحقل فارغ");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLightStatusBar();
    }

    private void fetchAd(String add_id) {

        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        if (TextUtils.isEmpty(session.getUserId())) {
            Call<GetPostResponseModel> call1 = apiService.getItem2(Integer.parseInt(add_id));

            call1.enqueue(new Callback<GetPostResponseModel>() {
                @Override
                public void onResponse(Call<GetPostResponseModel> call, Response<GetPostResponseModel> response) {
                    GetPostResponseModel responseModel = response.body();
                    AddDetailsActivity.this.responseModel = responseModel;
                    try {
                        sharUrl = responseModel.getData().getShare_url();

                    } catch (Exception e) {
                    }

                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);

                        if (session.isLogin() && responseModel.getData().getFavItemStatus().equals("1")) {
                            isLike = true;
                            likeBtn.setImageResource(R.drawable.ic_heart_filled);
                            settextcolor(favtxt, R.color.white);
                        } else {
                            isLike = false;
                            likeBtn.setImageResource(R.drawable.ic_heart);
                            settextcolor(favtxt, R.color.black);
                        }

                        if (session.isLogin() && responseModel.getData().getLikeItemStatus().equals("1")) {
                            isLikeAll = true;
                            icLike.setImageResource(R.drawable.ic_like);
                            settextcolor(liketxt, R.color.white);
                        } else {
                            isLikeAll = false;
                            icLike.setImageResource(R.drawable.ic_like_unchecked);
                            settextcolor(liketxt, R.color.black);
                        }


                        if (session.isLogin() && responseModel.getData().getReportStatus().equals("1")) {
                            isReported = true;
                            ivReport.setImageResource(R.drawable.ic_flag_filled);
                            settextcolor(reporttxt, R.color.white);
                        } else {
                            isReported = false;
                            ivReport.setImageResource(R.drawable.ic_flag);
                            settextcolor(reporttxt, R.color.black);
                        }

//                    if(session.isLogin() && responseModel.getData().getIs().equals("1")) {
//                        isLike=true;
//                        likeBtn.setImageResource(R.drawable.ic_heart_filled);
//                    } else {
//                        isLike=false;
//                        likeBtn.setImageResource(R.drawable.ic_heart);
//                    }

                        tvCount.setText(responseModel.getData().getLikesCount() + "");
                        if (!responseModel.getData().getLikesCount().equalsIgnoreCase("")) {
                            tvCount.setVisibility(View.VISIBLE);
                        }

                        if (!responseModel.getData().getFromUserId().equals(session.getUserId())) {
//                        CMLayout.setVisibility(View.VISIBLE);
                            CMLayout.setVisibility(View.GONE);
                            messageBtn.setOnClickListener(messageClickListener);
                        } else
                            CMLayout.setVisibility(View.GONE);


                        descriptionTv.setText(responseModel.getData().getItemDesc());
                        titleTv.setText(responseModel.getData().getItemTitle());
                        idUser.setText(responseModel.getData().getUsername());
                        userName = responseModel.getData().getUsername();
                        city.setText(responseModel.getData().getCity());
                        age.setText(responseModel.getData().getAge());
                        postid.setText(responseModel.getData().getId());
                        passport.setText(responseModel.getData().getPassport());
                        vaccine.setText(responseModel.getData().getVaccine_detail());
//                    country.setText(responseModel.getData().getCountry());
//                    date.setText(parseDate(responseModel.getData().getCreateAt()));

                        TimeShow timeShow = new TimeShow();
                        date.setText(timeShow.covertTimeToText(AddDetailsActivity.this, responseModel.getData().getCreateAt()));
//                    date.setText(parseDate(responseModel.getData().getCreateAt()));

                        if (responseModel.getData().getShowPhoneNumber() != null && responseModel.getData().getShowPhoneNumber().equals("1")) {
                            ivCall.setEnabled(true);
                        } else {
                            ivCall.setEnabled(false);
                        }

                        if (responseModel.getData().getShowMessage() != null && responseModel.getData().getShowMessage().equals("1")) {
                            ivChat.setEnabled(true);
                        } else {
                            ivChat.setEnabled(true);
                        }

                        if (responseModel.getData().getShowComments() != null && responseModel.getData().getShowComments().equals("1")) {
                            llComment.setVisibility(View.VISIBLE);
                        } else {
                            llComment.setVisibility(View.GONE);
                        }
                        if (responseModel.getData().getShowPhoneNumber() != null && responseModel.getData().getShowPhoneNumber().equals("1")) {
                            ivWhatsapp.setEnabled(true);
                        } else {
                            ivWhatsapp.setEnabled(true);

                        }


                        if (responseModel.getData().getShowMessage() != null && responseModel.getData().getShowMessage().equals("1")) {

                        }
//                    if(responseModel.getData().getShowPhoneNumber().equals("1")){
//
//                    }


                        List<String> images = new ArrayList<>();

                        adImage.setOnClickListener(adImageClickListener);


                        Log.i("akhjebfewfg", "hey:" + responseModel.getData().getImages().toString());
                        images.add(responseModel.getData().getImgUrl());
                        if (responseModel.getData().getImages().size() > 0) {


                            String[] imgSplitted = responseModel.getData().getImages().get(0).getFileName().split(",");

                            if (imgSplitted.length > 0) {
                                for (int i = 0; i < imgSplitted.length; i++) {
                                    images.add(imgSplitted[i]);
                                    DtoImageVideo obj = new DtoImageVideo();
                                    obj.setImage(true);
                                    obj.setUri(imgSplitted[i]);
                                    listImageView.add(obj);
                                }
                            }

                        }
                        if (responseModel.getData().getVideoUrl() != null && !!responseModel.getData().getVideoUrl().isEmpty()) {
                            DtoImageVideo obj = new DtoImageVideo();
                            obj.setImage(false);
                            obj.setUri(responseModel.getData().getVideoUrl());
                            listImageView.add(obj);
                        }
                        DtoImageVideo obj = new DtoImageVideo();
                        obj.setImage(true);
                        obj.setUri(responseModel.getData().getImgUrl());
                        listImageView.add(obj);
                        addSlider(listImageView);
                        adImage.setImageResource(Integer.parseInt(images.get(0)));

                        ArrayList<ImageModel> arrayList = new ArrayList<>();
                        ArrayList<RelatedAdModel> arrayListRA = new ArrayList<>();
                   /* for (int i = 0; i < responseModel.getData().getImages().size(); i++) {
                        arrayList.add(new ImageModel(responseModel.getData().getImages().get(i).toString()));
                    }*/

                        if (responseModel.getData().getImages().size() > 0) {
                            String[] imagesSplited = responseModel.getData().getImages().get(0).getFileName().split(",");
                            if (imagesSplited.length > 0) {
                                for (int i = 0; i < imagesSplited.length; i++) {
                                    arrayList.add(new ImageModel(imagesSplited[i]));
                                }
                            }
                        }

                        for (int j = 0; j < responseModel.getData().getrelatedAdImages().size(); j++) {
                            if (j == 6) {
                                break; //
                            }
                            arrayListRA.add(
                                    responseModel.getData().getrelatedAdImages().get(j));
//                                new ImageModel( "https://api.zoolifeshop.com/api/assets/images/" +responseModel.getData().getrelatedAdImages().get(j).getImgUrl()));
                        }

                        if (arrayList.size() > 0) {
                            imageLoaderAdapter = new ImageLoaderAdapter(AddDetailsActivity.this, arrayList);
                            more_imagesRV.setAdapter(imageLoaderAdapter);
//                        more_imagesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            more_imagesRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset_1);
                            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(3, spacingInPixels, true, 0);
                            more_imagesRV.addItemDecoration(itemDecoration);
                        }
                        if (arrayListRA.size() > 0) {
                            imageLoaderAdapterRA = new ImageLoaderAdapterRa(AddDetailsActivity.this, arrayListRA);
                            more_imagesRA.setAdapter(imageLoaderAdapterRA);
                        }
                    }
                    viewComments();
                }

                @Override
                public void onFailure(Call<GetPostResponseModel> call, Throwable t) {
                    t.printStackTrace();
                    String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    progress_circular.setVisibility(View.GONE);
                    viewComments();
                }
            });
        } else {


            Call<GetPostResponseModel> call = apiService.getItem(Integer.parseInt(session.getUserId()), Integer.parseInt(add_id));
            call.enqueue(new Callback<GetPostResponseModel>() {
                @Override
                public void onResponse(Call<GetPostResponseModel> call, Response<GetPostResponseModel> response) {
                    GetPostResponseModel responseModel = response.body();
                    AddDetailsActivity.this.responseModel = responseModel;
                    try {
                        sharUrl = responseModel.getData().getShare_url();

                    } catch (Exception e) {
                    }

                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);
                        Log.d("akhjebfewflkkg", "" + responseModel);
                        if (session.isLogin() && responseModel.getData().getFavItemStatus().equals("1")) {
                            isLike = true;
                            likeBtn.setImageResource(R.drawable.ic_heart_filled);
                            settextcolor(favtxt, R.color.white);
                        } else {
                            isLike = false;
                            likeBtn.setImageResource(R.drawable.ic_heart);
                            settextcolor(favtxt, R.color.black);
                        }

                        if (session.isLogin() && responseModel.getData().getLikeItemStatus().equals("1")) {
                            isLikeAll = true;
                            icLike.setImageResource(R.drawable.ic_like);
                            settextcolor(liketxt, R.color.white);
                        } else {
                            isLikeAll = false;
                            icLike.setImageResource(R.drawable.ic_like_unchecked);
                            settextcolor(liketxt, R.color.black);
                        }


                        if (session.isLogin() && responseModel.getData().getReportStatus().equals("1")) {
                            isReported = true;
                            ivReport.setImageResource(R.drawable.ic_flag_filled);
                            settextcolor(reporttxt, R.color.white);
                        } else {
                            isReported = false;
                            ivReport.setImageResource(R.drawable.ic_flag);
                            settextcolor(reporttxt, R.color.black);
                        }
//                    if(session.isLogin() && responseModel.getData().getIs().equals("1")) {
//                        isLike=true;
//                        likeBtn.setImageResource(R.drawable.ic_heart_filled);
//                    } else {
//                        isLike=false;
//                        likeBtn.setImageResource(R.drawable.ic_heart);
//                    }

                        tvCount.setText(responseModel.getData().getLikesCount() + " إعجاب ");
                        if (!responseModel.getData().getLikesCount().equalsIgnoreCase("")) {
                            tvCount.setVisibility(View.VISIBLE);
                        }

                        if (!responseModel.getData().getFromUserId().equals(session.getUserId())) {
//                        CMLayout.setVisibility(View.VISIBLE);
                            CMLayout.setVisibility(View.GONE);
                            messageBtn.setOnClickListener(messageClickListener);
                        } else
                            CMLayout.setVisibility(View.GONE);


                        descriptionTv.setText(responseModel.getData().getItemDesc());
                        titleTv.setText(responseModel.getData().getItemTitle());
                        idUser.setText(responseModel.getData().getUsername());
                        userName = responseModel.getData().getUsername();
                        city.setText(responseModel.getData().getCity() + "");
                        age.setText(responseModel.getData().getAge() + "");
                        postid.setText(responseModel.getData().getId() + "");
                        passport.setText(responseModel.getData().getPassport() + "");
                        vaccine.setText(responseModel.getData().getVaccine_detail() + "");
//                    country.setText(responseModel.getData().getCountry());
//                    date.setText(parseDate(responseModel.getData().getCreateAt()));
                        TimeShow timeShow = new TimeShow();
                        date.setText(timeShow.covertTimeToText(AddDetailsActivity.this, responseModel.getData().getCreateAt()));
//                    date.setText(parseDate(responseModel.getData().getCreateAt()));

                        if (responseModel.getData().getShowPhoneNumber() != null && responseModel.getData().getShowPhoneNumber().equals("1")) {
//                            ivCall.setBackgroundResource(R.drawable.ripple_effect_blue_detail_icons);
//                            DrawableCompat.setTint(ivCall.getDrawable(), ContextCompat.getColor(AddDetailsActivity.this, R.color.white));
                            ivCall.setEnabled(true);
                        } else {
//                            ivCall.setBackgroundResource(R.drawable.ripple_effect_white_detail_icons);
//                            DrawableCompat.setTint(ivCall.getDrawable(), ContextCompat.getColor(AddDetailsActivity.this, R.color.gray));
                            ivCall.setEnabled(false);
                        }

                        if (responseModel.getData().getShowMessage() != null && responseModel.getData().getShowMessage().equals("1")) {
                            ivChat.setEnabled(true);
                        } else {
                            ivChat.setEnabled(true);

                        }

                        if (responseModel.getData().getShowComments() != null && responseModel.getData().getShowComments().equals("1")) {
                            llComment.setVisibility(View.VISIBLE);
                        } else {
                            llComment.setVisibility(View.GONE);
                        }
                        if (responseModel.getData().getShowWhatsapp() != null && responseModel.getData().getShowWhatsapp().equals("1")) {
                            ivWhatsapp.setImageDrawable(ContextCompat.getDrawable(AddDetailsActivity.this, R.drawable.ic_whats_app_blue));
                            ivWhatsapp.setEnabled(true);
                        } else {
                            ivWhatsapp.setImageDrawable(ContextCompat.getDrawable(AddDetailsActivity.this, R.drawable.ic_whatsapp_icon));
                            ivWhatsapp.setEnabled(true);

                        }
//                    if(responseModel.getData().getShowPhoneNumber().equals("1")){
//
//                    }


                        List<String> images = new ArrayList<>();

                        adImage.setOnClickListener(adImageClickListener);


                        images.add(responseModel.getData().getImgUrl());

                        if (responseModel.getData().getImages().size() > 0) {
                            String[] imgSplitted = responseModel.getData().getImages().get(0).getFileName().split(",");

                            listImageView.clear();
                            if (imgSplitted.length > 0) {
                                for (int i = 0; i < imgSplitted.length; i++) {
                                    DtoImageVideo obj = new DtoImageVideo();
                                    obj.setImage(true);
                                    obj.setUri(imgSplitted[i]);
                                    listImageView.add(obj);
                                }
                            }
                        }
                        if (responseModel.getData().getVideoUrl() != null && !responseModel.getData().getVideoUrl().isEmpty()) {
                            DtoImageVideo obj = new DtoImageVideo();
                            obj.setImage(false);
                            obj.setUri(responseModel.getData().getVideoUrl());
                            listImageView.add(obj);
                        }
                        DtoImageVideo obj = new DtoImageVideo();
                        obj.setImage(true);
                        obj.setUri(responseModel.getData().getImgUrl());
                        listImageView.add(obj);
                        addSlider(listImageView);

                        ArrayList<ImageModel> arrayList = new ArrayList<>();
                        ArrayList<RelatedAdModel> arrayListRA = new ArrayList<>();

                   /* for (int i = 0; i < responseModel.getData().getImages().size(); i++) {
                        arrayList.add(new ImageModel(responseModel.getData().getImages().get(i).toString()));
                    }*/

                        if (responseModel.getData().getImages().size() > 0) {
                            String[] imagesSplited = responseModel.getData().getImages().get(0).getFileName().split(",");
                            if (imagesSplited.length > 0) {
                                for (int i = 0; i < imagesSplited.length; i++) {
                                    arrayList.add(new ImageModel(imagesSplited[i]));
                                }
                            }
                        }

                        for (int j = 0; j < responseModel.getData().getrelatedAdImages().size(); j++) {
                            if (j == 6) {
                                break; //
                            }
                            arrayListRA.add(
                                    responseModel.getData().getrelatedAdImages().get(j));
//                                new ImageModel( "https://api.zoolifeshop.com/api/assets/images/" +responseModel.getData().getrelatedAdImages().get(j).getImgUrl()));
                        }

                        if (arrayList.size() > 0) {
                            imageLoaderAdapter = new ImageLoaderAdapter(AddDetailsActivity.this, arrayList);
                            more_imagesRV.setAdapter(imageLoaderAdapter);
//                        more_imagesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            more_imagesRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset_1);
                            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(3, spacingInPixels, true, 0);
                            more_imagesRV.addItemDecoration(itemDecoration);
                        }
                        if (arrayListRA.size() > 0) {
                            imageLoaderAdapterRA = new ImageLoaderAdapterRa(AddDetailsActivity.this, arrayListRA);
                            more_imagesRA.setAdapter(imageLoaderAdapterRA);

                        }


                    }
                    viewComments();
                }

                @Override
                public void onFailure(Call<GetPostResponseModel> call, Throwable t) {
                    t.printStackTrace();
                    String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    progress_circular.setVisibility(View.GONE);
                    viewComments();
                }
            });


        }


    }

    private void settextcolor(TextView tv, int color) {
        tv.setTextColor(color);
    }

    private void addComment(String comment) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<AddCommentResponseModel> call = apiService.addComment(Integer.parseInt(session.getUserId()), Integer.parseInt(add_id), comment);
        call.enqueue(new Callback<AddCommentResponseModel>() {
            @Override
            public void onResponse(Call<AddCommentResponseModel> call, Response<AddCommentResponseModel> response) {
                AddCommentResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();
                    commentET.setText("");
                    viewComments();
                }
            }

            @Override
            public void onFailure(Call<AddCommentResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    void addSlider(ArrayList<DtoImageVideo> list) {
        SliderAdapter adapter = new SliderAdapter(AddDetailsActivity.this, list, this);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new LinePagerIndicatorDecoration());
    }

    private void doFavAdd(int isLike) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<FavResponseModel> call = apiService.favoruitItem(session.getUserId(), Integer.parseInt(add_id), isLike);
        call.enqueue(new Callback<FavResponseModel>() {
            @Override
            public void onResponse(Call<FavResponseModel> call, Response<FavResponseModel> response) {
                FavResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFailure(Call<FavResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }


    /*public Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor); // read timeout

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL5)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }*/

    private void doLikeAdd() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<NoDataResponseModel> call = apiService.likeItem(Integer.parseInt(add_id), Integer.parseInt(session.getUserId()));
        call.enqueue(new Callback<NoDataResponseModel>() {
            @Override
            public void onResponse(Call<NoDataResponseModel> call, Response<NoDataResponseModel> response) {


                icLike.setClickable(true);
                icLike.setEnabled(true);

                NoDataResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();
                    fetchAd("" + add_id);

                }
            }

            @Override
            public void onFailure(Call<NoDataResponseModel> call, Throwable t) {
                t.printStackTrace();
                icLike.setClickable(true);
                icLike.setEnabled(true);

                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    private void reportPost(String input) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<AddPostResponseModel> call = apiService.reportApi(input, add_id, session.getUserId(), input);
        call.enqueue(new Callback<AddPostResponseModel>() {
            @Override
            public void onResponse(Call<AddPostResponseModel> call, Response<AddPostResponseModel> response) {
                AddPostResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    ivReport.setImageResource(R.drawable.ic_flag_filled);

                    progress_circular.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    ivReport.setImageResource(R.drawable.ic_flag);
                    progress_circular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddPostResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
                ivReport.setImageResource(R.drawable.ic_flag);

            }
        });
    }

    private void viewComments() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<ViewCommentsResponseModel> call = apiService.listCommentByItem(add_id);
        call.enqueue(new Callback<ViewCommentsResponseModel>() {
            @Override
            public void onResponse(Call<ViewCommentsResponseModel> call, Response<ViewCommentsResponseModel> response) {
                ViewCommentsResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);

                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);

                        ArrayList<CommentModel> arrayList = new ArrayList<>();

                        for (int i = 0; i < responseModel.getData().size(); i++) {
                            if (i < 3) {
                                DataItem dataItem = responseModel.getData().get(i);
//                                arrayList.add(new CommentModel(dataItem.getMessage(), String.valueOf(dataItem.getId()), userName, dataItem.getCo()));
                                arrayList.add(new CommentModel(userName, String.valueOf(dataItem.getId()), userName, dataItem.getCo(), dataItem.getMessage() + ""));

                            } else {
                                break;
                            }


                        }
                        if (arrayList.size() > 0) {
                            commentsAdapter = new CommentsAdapter(getApplicationContext(), arrayList);
                            commentRV.setAdapter(commentsAdapter);
                            commentRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        } else
                            progress_circular.setVisibility(View.GONE);
                    } else {
                        // infoDialog("Server Error.");
                        progress_circular.setVisibility(View.GONE);
                    }


                }
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ViewCommentsResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    private View.OnClickListener adImageClickListener = v -> {


        Log.i("askdhbvwrv", "sajvberwv");
        Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.photo_layout);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = builder.findViewById(R.id.image);
        ProgressBar progressBar = builder.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(adImageUrl)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        builder.show();
    };

    private String parseDate(String createdAt) {
        String date = createdAt.split(" ")[0];
        String time = createdAt.split(" ")[1];

        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]) - 1;
        int day = Integer.parseInt(date.split("-")[2]);

        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        int second = Integer.parseInt(time.split(":")[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);

        Date createdAtDate = cal.getTime();

        return new SimpleDateFormat("EEEE, MMM d, yyyy").format(createdAtDate);
    }


    public void openWhatsApp(String number, String message) {
        try {

            String msgurl = "https://api.whatsapp.com/send?phone=" + number + "&text=" + message;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(msgurl));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "Whatsapp app not found on your device", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void getUserProfileApi() {

//        String username = responseModel.getData().getEmail().equals(session.getEmail()) ? session.getEmail() : responseModel.getData().getEmail();
        String username = responseModel.getData().getPhone().equals(session.getPhone()) ? session.getPhone() : responseModel.getData().getPhone();
//        String username = group.getSenderEmail().equals(session.getEmail()) ? group.getRecipientEmail() : group.getSenderEmail();


        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<GetUserProfileResponseModel> call = apiService.getUserProfile(responseModel.getData().getUserid());
        call.enqueue(new Callback<GetUserProfileResponseModel>() {
            @Override
            public void onResponse(Call<GetUserProfileResponseModel> call, Response<GetUserProfileResponseModel> response) {
                GetUserProfileResponseModel responseModel1 = response.body();
                if (response.isSuccessful() && responseModel1.getData() != null) {
                    Intent chatIntent = new Intent(AddDetailsActivity.this, ChatActivity.class);
//                    chatIntent.putExtra(ChatActivity.AD_ID, group.getAdId());
//                    chatIntent.putExtra(ChatActivity.AD_TITLE, group.getAdTitle());
//                    chatIntent.putExtra(ChatActivity.AD_CREATED_USER, group.getAdCreatedUser());

                    chatIntent.putExtra(ChatActivity.AD_ID, responseModel.getData().getId());
                    chatIntent.putExtra(ChatActivity.AD_TITLE, responseModel.getData().getItemTitle());
                    chatIntent.putExtra(ChatActivity.AD_CREATED_USER, responseModel.getData().getUsername());
                    chatIntent.putExtra(ChatActivity.USER_OBJ, responseModel1);
                    chatIntent.putExtra(ChatActivity.GROUP_ID, "");
                    startActivity(chatIntent);

                } else {
                    Log.d("AddDetailsActivity", "Server Error.");
                    try {
                        Toast.makeText(AddDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserProfileResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onLikeClick(int position) {
        AuctionPostDetailsActivity.list.postValue(listImageView);
        Intent chatIntent = new Intent(AddDetailsActivity.this, FullImageViewActivity.class);
        chatIntent.putExtra("position", "" + position);
        startActivity(chatIntent);

    }
}