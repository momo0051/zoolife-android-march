package com.zoolife.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.custom.sliderimage.logic.SliderImage;
import com.squareup.picasso.Picasso;
import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.AddComment.AddCommentResponseModel;
import com.zoolife.app.ResponseModel.AddPost.AddPostResponseModel;
import com.zoolife.app.ResponseModel.Bid.BidModel;
import com.zoolife.app.ResponseModel.FavModel.FavResponseModel;
import com.zoolife.app.ResponseModel.GetPost.GetPostResponseModel;
import com.zoolife.app.ResponseModel.GetUserProfile.GetUserProfileResponseModel;
import com.zoolife.app.ResponseModel.NoDataResponseModel;
import com.zoolife.app.adapter.CommentsAdapter;
import com.zoolife.app.adapter.ImageLoaderAdapter;
import com.zoolife.app.cstmslider.LinePagerIndicatorDecoration;
import com.zoolife.app.models.CommentModel;
import com.zoolife.app.models.ImageModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;
import com.zoolife.app.cstmslider.DtoImageVideo;
import com.zoolife.app.cstmslider.FullImageViewActivity;
import com.zoolife.app.cstmslider.SliderAdapter;
import com.zoolife.app.utility.ItemOffsetDecoration;
import com.zoolife.app.utility.TimeShow;
import com.zoolife.app.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuctionPostDetailsActivity extends AppBaseActivity implements SliderAdapter.OnClickListener {
    ProgressBar progress_circular;
    CommentsAdapter commentsAdapter;
    String add_id = "", from = "";
    TextView descriptionTv, titleTv, idUser, date, city, titlebid; /*,country;*/
    SliderImage adImage;
    RecyclerView more_imagesRV;
    RecyclerView commentRV;
    ImageLoaderAdapter imageLoaderAdapter;
    private TextView messageBtn;

    public static MutableLiveData<ArrayList<DtoImageVideo>> list = new MutableLiveData<>();
    private ArrayList<DtoImageVideo> listImageView = new ArrayList<>();
    private GetPostResponseModel responseModel;
    String sharUrl = "";
    String midBid = "-1";


    private String adImageUrl;
    private LinearLayout llReport;
    private ImageView ivCall, ivWhatsapp, ivChat;
    private static Retrofit retrofit = null;
    private String userName;
    private static final String TAG = "AuctionPostDetailsActivity";
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
                            Intent chatIntent = new Intent(AuctionPostDetailsActivity.this, ChatActivity.class);
                            chatIntent.putExtra(ChatActivity.AD_ID, AuctionPostDetailsActivity.this.responseModel.getData().getId());
                            chatIntent.putExtra(ChatActivity.AD_TITLE, AuctionPostDetailsActivity.this.responseModel.getData().getItemTitle());
                            chatIntent.putExtra(ChatActivity.AD_CREATED_USER, AuctionPostDetailsActivity.this.responseModel.getData().getUsername());
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
            Toast.makeText(AuctionPostDetailsActivity.this, "You have to login first to chat", Toast.LENGTH_SHORT).show();
        }
    };
    private TextView age, passport, vaccine, postid, sex;
    private EditText edt_bid;
    private ImageView iv_share;
    private RecyclerView rv;
    private Button placebid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //forceRTLIfSupported();
        add_id = getIntent().getStringExtra("id");


//        Log.i("showwhhatsapp", "getallpost: setting " + data.get(getAdapterPosition()).getShowWhatsapp());
        Log.i("asuydfgwrge", ":" + add_id);

        setContentView(R.layout.activity_auction_details);

        commentRV = findViewById(R.id.commentRV);
        titlebid = findViewById(R.id.titlebid);
        commentRV.setLayoutManager(new LinearLayoutManager(this));
        ivCall = findViewById(R.id.iv_call);
        ivWhatsapp = findViewById(R.id.iv_whatsapp);
        llReport = findViewById(R.id.llReport);
        ivChat = findViewById(R.id.iv_chat);
        edt_bid = findViewById(R.id.edt_bid);
        iv_share = findViewById(R.id.iv_share);
        rv = findViewById(R.id.rvSlider);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(rv);
        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPost("Reported");
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharUrl.isEmpty()) {
                    Utils.shareApp(AuctionPostDetailsActivity.this, sharUrl);
                }
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

        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseModel.getData().getPhone() != null && !responseModel.getData().getPhone().isEmpty()) {
                    openWhatsApp(responseModel.getData().getPhone(), "");
                } else {

                }

            }
        });

        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserProfileApi();

            }
        });

        descriptionTv = findViewById(R.id.descriptionTv);
        titleTv = findViewById(R.id.titleTv);
        progress_circular = findViewById(R.id.progress_circular);
        adImage = findViewById(R.id.addImage);
        idUser = findViewById(R.id.idUser);
        date = findViewById(R.id.date);
        city = findViewById(R.id.city);
//        country = findViewById(R.id.country);
        more_imagesRV = findViewById(R.id.more_imagesRV);
        messageBtn = findViewById(R.id.messageBtn);
        age = findViewById(R.id.age);
        passport = findViewById(R.id.passport);
        postid = findViewById(R.id.postid);
        vaccine = findViewById(R.id.vaccine);
        sex = findViewById(R.id.sex);


        from = getIntent().getStringExtra("from");

        fetchAd("" + add_id);


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        placebid = findViewById(R.id.placebid);
        placebid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_bid.getText().length() > 0) {
                    if (midBid != "") {
                        if (Integer.parseInt(midBid) < Integer.parseInt(edt_bid.getText().toString())) {
                            callbid(edt_bid.getText().toString());
                        } else {
                            Toast.makeText(AuctionPostDetailsActivity.this, "Place Bide more then mid bide", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        callbid(edt_bid.getText().toString());
                    }
                } else {
                    Toast.makeText(AuctionPostDetailsActivity.this, "Refresh the page", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void callbid(String bid) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<NoDataResponseModel> call = apiService.add_bid(Integer.parseInt(add_id), Integer.parseInt(session.getUserId()), bid);
        call.enqueue(new Callback<NoDataResponseModel>() {
            @Override
            public void onResponse(Call<NoDataResponseModel> call, Response<NoDataResponseModel> response) {
                NoDataResponseModel responseModel = response.body();
                progress_circular.setVisibility(View.GONE);
                if (responseModel != null && !responseModel.isError()) {
                    viewComments();
                    Toast.makeText(AuctionPostDetailsActivity.this, responseModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NoDataResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(AuctionPostDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLightStatusBar();
    }

    void addSlider(ArrayList<DtoImageVideo> list) {
        SliderAdapter adapter = new SliderAdapter(AuctionPostDetailsActivity.this, list, this);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new LinePagerIndicatorDecoration());
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
                    AuctionPostDetailsActivity.this.responseModel = responseModel;
                    try {
                        sharUrl = responseModel.getData().getShare_url();

                    } catch (Exception e) {
                    }

                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);
                        descriptionTv.setText(responseModel.getData().getItemDesc());
                        titleTv.setText(responseModel.getData().getItemTitle());
                        idUser.setText(responseModel.getData().getUsername());
                        userName = responseModel.getData().getUsername();
                        city.setText(responseModel.getData().getCity());
                        age.setText(responseModel.getData().getAge());
                        postid.setText(responseModel.getData().getId());
                        passport.setText(responseModel.getData().getPassport());
                        vaccine.setText(responseModel.getData().getVaccine_detail());
                        sex.setText(responseModel.getData().getSex());
                        titlebid.setText(getString(R.string.mid_bide) + responseModel.getData().getMax_bid());

//                        TimeShow timeShow = new TimeShow();
//                        date.setText(timeShow.covertTimeToText(AuctionPostDetailsActivity.this, responseModel.getData().getCreateAt()));
                        countDownStart(responseModel.getData().getAuction_expiry_time(), date);
                        if (responseModel.getData().getShowPhoneNumber() != null && responseModel.getData().getShowPhoneNumber().equals("1")) {
                            ivCall.setEnabled(true);
                        } else {
                            ivCall.setEnabled(true);
                        }

                        if (responseModel.getData().getShowMessage() != null && responseModel.getData().getShowMessage().equals("1")) {
                            ivChat.setEnabled(true);
                        } else {
                            ivChat.setEnabled(true);
                        }

                        if (responseModel.getData().getShowPhoneNumber() != null && responseModel.getData().getShowPhoneNumber().equals("1")) {
                            ivWhatsapp.setEnabled(true);
                        } else {
                            ivWhatsapp.setEnabled(true);

                        }


                        if (responseModel.getData().getShowMessage() != null && responseModel.getData().getShowMessage().equals("1")) {

                        }


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
                        adImage.setItems(images);


                        ArrayList<ImageModel> arrayList = new ArrayList<>();


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
                        }

                        if (arrayList.size() > 0) {
                            imageLoaderAdapter = new ImageLoaderAdapter(AuctionPostDetailsActivity.this, arrayList);
                            more_imagesRV.setAdapter(imageLoaderAdapter);
//                        more_imagesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            more_imagesRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset_1);
                            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(3, spacingInPixels, true, 0);
                            more_imagesRV.addItemDecoration(itemDecoration);
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
                    AuctionPostDetailsActivity.this.responseModel = responseModel;
                    try {
                        sharUrl = responseModel.getData().getShare_url();

                    } catch (Exception e) {
                    }

                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);
                        Log.d("akhjebfewflkkg", "" + responseModel);
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
//                        TimeShow timeShow = new TimeShow();
//                        date.setText(timeShow.covertTimeToText(AuctionPostDetailsActivity.this, responseModel.getData().getCreateAt()));
                        countDownStart(responseModel.getData().getAuction_expiry_time(), date);

                        if (responseModel.getData().getShowPhoneNumber() != null && responseModel.getData().getShowPhoneNumber().equals("1")) {
                            ivCall.setEnabled(true);
                        } else {
                            ivCall.setEnabled(true);
                        }

                        if (responseModel.getData().getShowMessage() != null && responseModel.getData().getShowMessage().equals("1")) {
                            //    ivChat.setBackgroundResource(R.drawable.ripple_effect_blue_detail_icons);
                            //  DrawableCompat.setTint(ivChat.getDrawable(), ContextCompat.getColor(AddDetailsActivity.this, R.color.white));
                            ivChat.setEnabled(true);
                        } else {
                            //  ivChat.setBackgroundResource(R.drawable.ripple_effect_white_detail_icons);
                            // DrawableCompat.setTint(ivChat.getDrawable(), ContextCompat.getColor(AddDetailsActivity.this, R.color.gray));
                            ivChat.setEnabled(true);

                        }

                        if (responseModel.getData().getShowWhatsapp() != null && responseModel.getData().getShowWhatsapp().equals("1")) {
//                            ivWhatsapp.setBackgroundResource(R.drawable.ripple_effect_blue_detail_icons);
                            ivWhatsapp.setImageDrawable(ContextCompat.getDrawable(AuctionPostDetailsActivity.this, R.drawable.ic_whats_app_blue));
                            ivWhatsapp.setEnabled(true);
                        } else {
//                            ivWhatsapp.setBackgroundResource(R.drawable.ripple_effect_white_detail_icons);
                            ivWhatsapp.setImageDrawable(ContextCompat.getDrawable(AuctionPostDetailsActivity.this, R.drawable.ic_whatsapp_icon));
                            ivWhatsapp.setEnabled(true);

                        }


                        List<String> images = new ArrayList<>();
                        adImage.setOnClickListener(adImageClickListener);
                        images.add(responseModel.getData().getImgUrl());
                        DtoImageVideo obj = new DtoImageVideo();
                        obj.setImage(true);
                        obj.setUri(responseModel.getData().getImgUrl());
                        listImageView.add(obj);
                        if (responseModel.getData().getImages().size() > 0) {


                            String[] imgSplitted = responseModel.getData().getImages().get(0).getFileName().split(",");
                            if (imgSplitted.length > 0) {
                                for (int i = 0; i < imgSplitted.length; i++) {
                                    DtoImageVideo ob = new DtoImageVideo();
                                    ob.setImage(true);
                                    ob.setUri(imgSplitted[i]);
                                    listImageView.add(ob);
                                }
                            }
                        }
                        if (!responseModel.getData().getVideoUrl().isEmpty()) {
                            DtoImageVideo obj1 = new DtoImageVideo();
                            obj1.setImage(false);
                            obj1.setUri(responseModel.getData().getVideoUrl());
                            listImageView.add(obj1);
                        }
                        addSlider(listImageView);
//                        adImage.setItems(images);

                        ArrayList<ImageModel> arrayList = new ArrayList<>();

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
                        }

                        if (arrayList.size() > 0) {
                            imageLoaderAdapter = new ImageLoaderAdapter(AuctionPostDetailsActivity.this, arrayList);
                            more_imagesRV.setAdapter(imageLoaderAdapter);
//                        more_imagesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            more_imagesRV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset_1);
                            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(3, spacingInPixels, true, 0);
                            more_imagesRV.addItemDecoration(itemDecoration);
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


    private void doLikeAdd() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<NoDataResponseModel> call = apiService.likeItem(Integer.parseInt(add_id), Integer.parseInt(session.getUserId()));
        call.enqueue(new Callback<NoDataResponseModel>() {
            @Override
            public void onResponse(Call<NoDataResponseModel> call, Response<NoDataResponseModel> response) {


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


                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    private void reportPost(String input) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<AddPostResponseModel> call = apiService.reportApi("report", add_id, session.getUserId(), input);
        call.enqueue(new Callback<AddPostResponseModel>() {
            @Override
            public void onResponse(Call<AddPostResponseModel> call, Response<AddPostResponseModel> response) {
                AddPostResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {


                    progress_circular.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();

                } else {

                    progress_circular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddPostResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);


            }
        });
    }

    private void viewComments() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<BidModel> call = apiService.getBide(add_id);
        call.enqueue(new Callback<BidModel>() {
            @Override
            public void onResponse(Call<BidModel> call, Response<BidModel> response) {
                BidModel responseModel = response.body();
                if (responseModel != null && !responseModel.getError()) {
                    progress_circular.setVisibility(View.GONE);

                    if (responseModel != null && !responseModel.getError()) {
                        progress_circular.setVisibility(View.GONE);

                        ArrayList<CommentModel> arrayList = new ArrayList<>();

                        for (int i = 0; i < responseModel.getData().getData().size(); i++) {
                            BidModel.MData dataItem = responseModel.getData().getData().get(i);
                            arrayList.add(new CommentModel(dataItem.getUsername(), String.valueOf(dataItem.getId()), dataItem.getUsername()/*userName*/, dataItem.getCreatedAt(), dataItem.getBidAmount() + ""));
                            Log.e("Main_tags", "onResponse: "+dataItem.getCreatedAt() );
                        }
                        if (arrayList.size() > 0) {
                            commentsAdapter = new CommentsAdapter(getApplicationContext(), arrayList);
                            commentRV.setAdapter(commentsAdapter);

                            titlebid.setText(getString(R.string.mid_bide) + arrayList.get(0).bid);
                            midBid = arrayList.get(0).bid;
                        } else
                            progress_circular.setVisibility(View.GONE);
                    } else {
                        progress_circular.setVisibility(View.GONE);
                    }
                }
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<BidModel> call, Throwable t) {
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
                    Intent chatIntent = new Intent(AuctionPostDetailsActivity.this, ChatActivity.class);
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
                        Toast.makeText(AuctionPostDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserProfileResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AuctionPostDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onLikeClick(int position) {
        list.postValue(listImageView);
        Intent chatIntent = new Intent(AuctionPostDetailsActivity.this, FullImageViewActivity.class);
        chatIntent.putExtra("position", "" + position);
        startActivity(chatIntent);
    }

    private void countDownStart(String EVENT_DATE_TIME, TextView tv) {
        String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeShow timeShow = new TimeShow();
                    if (timeShow.convertDate(EVENT_DATE_TIME, AuctionPostDetailsActivity.this).contains(getString(R.string.auction_close))) {
                        tv.setText(timeShow.convertDate(EVENT_DATE_TIME, AuctionPostDetailsActivity.this));
                        placebid.setEnabled(false);
                        placebid.setText(getString(R.string.auction_close));
                        edt_bid.setText(getString(R.string.auction_close));
                        edt_bid.setEnabled(false);
                        return;
                    }
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
                        String time = String.format(getString(R.string.days), Days) + String.format(getString(R.string.hrs), Hours) +
                                String.format(getString(R.string.mnt), Minutes) + String.format(getString(R.string.sec), Seconds);
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