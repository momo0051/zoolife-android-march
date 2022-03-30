package com.zoolife.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.AddPost.AddPostResponseModel;
import com.zoolife.app.ResponseModel.Category.DataItem;
import com.zoolife.app.ResponseModel.GetPost.GetPostResponseModel;
import com.zoolife.app.ResponseModel.NoDataResponseModel;
import com.zoolife.app.ResponseModel.SubCategory.SubCategoryResponseModel;
import com.zoolife.app.adapter.AdapterAdsImages;
import com.zoolife.app.interfaces.ImageListener;
import com.zoolife.app.models.CategoryModel;
import com.zoolife.app.models.SubCategoryModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;
import com.zoolife.app.utility.Utils;
import com.zoolife.app.view.SquareImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdActivity extends AppBaseActivity
        implements AdapterView.OnItemSelectedListener
        , View.OnClickListener, ImageListener, AdapterAdsImages.ClickToRemove {

    private boolean editMode = false;

    private String editId;

    TextView imagesDelete;
    RecyclerView recyclerView;
    List<CategoryModel> categoryModelList;
    Spinner location_spinner, spinnerChoiceCategory, spinnerChoiceSubCategory;
    Button adContinueBtn;
    Button adDeleteBtn;
    String[] categories = {"طيور", "مواشي", "حيوانات اليفة", "حيوانات بحرية", "غذاء الحيوانات", "العيادة البيطرية"};
    ProgressBar progress_circular;
    String location1;
    List<DataItem> categoryList = new ArrayList<>();
    List<com.zoolife.app.ResponseModel.SubCategory.DataItem> subCategoryList = new ArrayList<>();
    DataItem selectedCatItem;
    com.zoolife.app.ResponseModel.SubCategory.DataItem selectedSubCatItem;
    String sub_category;
    String category;
    EditText titleET, descriptionET;
    CheckBox cb1, cb2, cb3, cb4;
    int phone = 0, message = 0, comment = 0, whatsApp = 0;
    int cat, subCat, loc;
    Switch showPhone;
    //    SquareImageView coverImage;
    SquareImageView tempImage;
    SquareImageView choiceItemImg1, choiceItemImg2, choiceItemImg3, choiceItemImg4;
    private static final String IMAGE_DIRECTORY = "/Zoo";
    private int GALLERY = 1, CAMERA = 2;
    ArrayList<String> filePaths = new ArrayList<>();
    String coverImgPath = "";
    boolean isCoverImg = false;
    //    LinearLayout addImagesLyt;
    RecyclerView adsImagesList;
    int PICK_IMAGE_MULTIPLE = 11;
    int PICK_VIDEO = 101;


    List<Uri> adsImages = new ArrayList<>();
    List<Uri> adsImagesPath = new ArrayList<>();
    Uri videoPath;
    String imageEncoded;
    List<String> imagesEncodedList = new ArrayList<>();
    AdapterAdsImages adapterAdsImages;
    List<String> sub_categories;
    String priority = "0";

    private String id = "";
    private EditText age, vaccine, price;
    private Button female, male, yes, no;
    private String sex = "female", passport = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forceRTLIfSupported();

        Log.i("oncreateCalled", "Addadactivity");
        setContentView(R.layout.activity_add_ad);
        requestMultiplePermissions();
        allowPermission();
//        defaultCoverImage();

        //getting data from previous activity
        if (getIntent() != null && getIntent().getBooleanExtra("edit", false)) {
            editMode = true;
            editId = getIntent().getStringExtra("editId");
            priority = getIntent().getStringExtra("priority");

        }

        adDeleteBtn = findViewById(R.id.adDeleteBtn);
        imagesDelete = findViewById(R.id.imagesDelete);

        // Spinner element
        progress_circular = findViewById(R.id.progress_circular);
//        coverImage = findViewById(R.id.coverImg);
        adsImagesList = findViewById(R.id.add_imagesRV);
//        addImagesLyt = findViewById(R.id.adImagesContainer);
        choiceItemImg1 = findViewById(R.id.choiceItemImg1);
        choiceItemImg2 = findViewById(R.id.choiceItemImg2);
        choiceItemImg3 = findViewById(R.id.choiceItemImg3);
        choiceItemImg4 = findViewById(R.id.choiceItemImg4);

        choiceItemImg1.setOnClickListener(this);
        choiceItemImg2.setOnClickListener(this);
        choiceItemImg3.setOnClickListener(this);
        choiceItemImg4.setOnClickListener(this);
//        coverImage.setOnClickListener(this);
//        addImagesLyt.setOnClickListener(this);


        showPhone = findViewById(R.id.showPhone);

        titleET = findViewById(R.id.titleET);
        descriptionET = findViewById(R.id.descriptionET);


        cb1 = findViewById(R.id.phoneCB);
        cb2 = findViewById(R.id.messageCB);
        cb3 = findViewById(R.id.commentCB);
        cb4 = findViewById(R.id.whatsappCB);

        age = findViewById(R.id.age);
        female = findViewById(R.id.female);
        male = findViewById(R.id.male);
        vaccine = findViewById(R.id.vaccine);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        passportClick();
        sexClick();


        Log.i("asjhvbweg", "asjhbvfwe");


        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    phone = 1;
                } else {
                    phone = 0;
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                if (bl) {
                    message = 1;
                } else {
                    message = 0;
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                if (bl) {
                    comment = 1;
                } else {
                    comment = 0;
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    whatsApp = 1;
                } else {
                    whatsApp = 0;
                }
            }
        });

        spinnerChoiceCategory = (Spinner) findViewById(R.id.category_spinner);
        spinnerChoiceCategory.setOnItemSelectedListener(this);


        location_spinner = (Spinner) findViewById(R.id.location_spinner);
        location_spinner.setOnItemSelectedListener(this);

        spinnerChoiceSubCategory = (Spinner) findViewById(R.id.sub_category_spinner);
//        spinnerChoiceSubCategory.setOnItemSelectedListener(this);
        spinnerChoiceSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_category = sub_categories.get(position);
                selectedSubCatItem = subCategoryList.get(position);
                subCat = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sub_categories = new ArrayList<String>();


        List<String> location = new ArrayList<String>();


        location.addAll(Arrays.asList(getResources().getStringArray(R.array.city)));

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location_spinner.setAdapter(dataAdapter2);
        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location1 = location.get(position);
                loc = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.adContinueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {


                    try {
                        if (editMode) {
                            updatePost();
                        } else {
                            addPost();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        adDeleteBtn.setOnClickListener(v -> deleteApi(editId));

//        imagesDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        imagesDelete.setOnClickListener(v -> deleteImages(editId));

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        copyAssets();

        if (editMode) {
            fetchData();
            adDeleteBtn.setVisibility(View.VISIBLE);
            imagesDelete.setVisibility(View.VISIBLE);
        } else {
            setCategorySpinner();
            adDeleteBtn.setVisibility(View.GONE);
            imagesDelete.setVisibility(View.GONE);
        }

        showImages( );
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLightStatusBar();
    }

    int count = 0;

    private void setCategorySpinner() {
        List<String> categories = new ArrayList<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        int selected = 0;
        boolean found = false;
        int i = 0;
        if (AppBaseActivity.categories != null) {
            for (DataItem categoryItem : AppBaseActivity.categories.getData()) {
                categories.add(categoryItem.getTitle());
                categoryList.add(categoryItem);
                if (selectedCategory.length() > 0 && categoryItem.getTitle().compareTo(selectedCategory) == 0) {
                    selected = i;
                    found = true;
                }
                hashMap.put(categoryItem.getId(), categoryItem.getTitle());
                i++;
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChoiceCategory.setAdapter(dataAdapter);
        spinnerChoiceCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (editMode & count == 0) {
                    category = categories.get(position);
                    spinnerChoiceCategory.setSelection(categories.indexOf(hashMap.get(cat)));
                    getSubCategory(cat);
                    count++;
                } else {
                    category = categories.get(position);
                    selectedCatItem = categoryList.get(position);
                    for (int val : hashMap.keySet()) {
                        if (category.contains(hashMap.get(val))) {
                            cat = val;
                        }
                    }
                    getSubCategory(selectedCatItem.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (found) {
            spinnerChoiceCategory.setSelection(selected);
            getSubCategory(selectedCatItem.getId());
        }


    }

    private void sexClick() {
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackgroundResource(R.drawable.bg_button);
                male.setBackgroundResource(R.drawable.bg_spinner);
                sex = "female";
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.bg_button);
                female.setBackgroundResource(R.drawable.bg_spinner);
                sex = "male";
            }
        });
    }

    private void passportClick() {
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes.setBackgroundResource(R.drawable.bg_button);
                no.setBackgroundResource(R.drawable.bg_spinner);
                passport = "yes";
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no.setBackgroundResource(R.drawable.bg_button);
                yes.setBackgroundResource(R.drawable.bg_spinner);
                passport = "no";
            }
        });
    }

    public void getSubCategory(int cat_id) {
        progress_circular.setVisibility(View.VISIBLE);

        Log.i("getSubCategory", cat_id + "");
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<SubCategoryResponseModel> call = apiService.getSubCategory(cat_id);
        call.enqueue(new Callback<SubCategoryResponseModel>() {
            @Override
            public void onResponse(Call<SubCategoryResponseModel> call, Response<SubCategoryResponseModel> response) {
                SubCategoryResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);

                    ArrayList<SubCategoryModel> arrayList = new ArrayList<>();
                    sub_categories = new ArrayList<>();
                    subCategoryList = new ArrayList<>();

                    Log.i("getSubCategory", responseModel.getData().size() + " size");

                    String catName = "";

                    if (responseModel.getData() != null) {
                        for (int i = 0; i < responseModel.getData().size(); i++) {
                            if (responseModel.getData().get(i).getTitle() != null) {
                                Log.i("getSubCategory", responseModel.getData().get(i).getTitle() + " titile  ");
                                Log.i("getSubCategory1233", responseModel.getData().get(i).getId() + " ----   " + subCategory);
                                if (responseModel.getData().get(i).getId().compareTo(subCategory) == 0) {
                                    catName = responseModel.getData().get(i).getTitle();
                                }
                                sub_categories.add(responseModel.getData().get(i).getTitle());
                                subCategoryList.add(responseModel.getData().get(i));
                            }
                        }
//                        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(AddAdActivity.this, android.R.layout.simple_spinner_item, sub_categories);
//                        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerChoiceSubCategory.setAdapter(dataAdapter1);

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (AddAdActivity.this, android.R.layout.simple_spinner_item, sub_categories);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerChoiceSubCategory.setAdapter(dataAdapter);

                        Log.i("getSubCategory", catName + "   subCategory");

                        for (int i = 0; i < sub_categories.size(); i++) {
                            if (catName.compareTo(sub_categories.get(i)) == 0) {
                                Log.i("getSubCategory", i + " found ");
                                spinnerChoiceSubCategory.setSelection(i);
                                break;
                            }
                        }


                    } else {
                        Toast.makeText(AddAdActivity.this, "No Record Found!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    // infoDialog("Server Error.");
                    progress_circular.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<SubCategoryResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(AddAdActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    private void allowPermission() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isValid() {

        if (location1.isEmpty()) {
            Toast.makeText(getApplicationContext(), "اختر موقعا", Toast.LENGTH_LONG).show();
            return false;
        } else if (category.isEmpty()) {
            Toast.makeText(getApplicationContext(), "اختر الفئة", Toast.LENGTH_LONG).show();
            return false;
        } else if (titleET.getText().toString().isEmpty()) {
            titleET.setError("أدخل العنوان");
            titleET.requestFocus();
            return false;
        } else if (descriptionET.getText().toString().isEmpty()) {
            descriptionET.setError("أدخل الوصف");
            descriptionET.requestFocus();
            return false;
        }



        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //   Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void addPost() {
        try {
            progress_circular.setVisibility(View.VISIBLE);

            ApiService apiService = ApiClient.getClient(this).create(ApiService.class);


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("user_id", session.getUserId());
            builder.addFormDataPart("fromUserId", session.getUserId());
            builder.addFormDataPart("priority", "0");
            builder.addFormDataPart("username", session.getEmail());
            builder.addFormDataPart("location", location1);
            builder.addFormDataPart("itemTitle", titleET.getText().toString());
            builder.addFormDataPart("itemDesc", descriptionET.getText().toString());
            builder.addFormDataPart("category", "" + cat);
            builder.addFormDataPart("subCategory", "" + subCat);
            builder.addFormDataPart("showComments", "" + comment);
            builder.addFormDataPart("showPhoneNumber", "" + phone);
            builder.addFormDataPart("showMessage", "" + message);
            builder.addFormDataPart("showWhatsapp", "" + whatsApp);
            builder.addFormDataPart("city", location1);
            builder.addFormDataPart("age", age.getText().toString());
            builder.addFormDataPart("sex", sex);
            builder.addFormDataPart("passport", passport);
            builder.addFormDataPart("vaccine_detail", vaccine.getText().toString());
            builder.addFormDataPart("country", "المملكة العربية السعودية");


            Log.i("akwshbjdvwrv", ":" + comment + "" + phone + "" + message + "" + whatsApp);

            Log.i("askjhdbvwdv", ":" + whatsApp);

            Log.i("post_update", adsImagesPath.size() + " --- adsImagesPath.size()");
            if (adsImagesPath.size() == 0) {

                File coverImgFile = new File(getFilesDir(), "placeholder.png");
                builder.addFormDataPart("imgUrl", "placeholder.png", RequestBody.create(MediaType.parse("multipart/form-data"), coverImgFile));
            } else {
                File coverImgFile = Utils.prepareFilePart(AddAdActivity.this, adsImagesPath.get(0));
                builder.addFormDataPart("imgUrl", coverImgFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), coverImgFile));

                for (int i = 0; i < adsImagesPath.size(); i++) {
                    Log.i("post_update", adsImagesPath.get(i) + " --- filesave");
                    File file = Utils.prepareFilePart(AddAdActivity.this, adsImagesPath.get(i));
                    builder.addFormDataPart("images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                }
            }
            if (videoPath != null) {
                File file = Utils.prepareFilePart(AddAdActivity.this, videoPath);
                builder.addFormDataPart("videoUrl", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }


            MultipartBody requestBody = builder.build();


            Call<AddPostResponseModel> call = apiService.addPost1(requestBody);
            call.enqueue(new Callback<AddPostResponseModel>() {
                @Override
                public void onResponse(Call<AddPostResponseModel> call, Response<AddPostResponseModel> response) {
                    AddPostResponseModel responseModel = response.body();
                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        // infoDialog("Server Error.");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePost() {
        try {
            progress_circular.setVisibility(View.VISIBLE);

            ApiService apiService = ApiClient.getClient(this).create(ApiService.class);


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("item_id", id);
            builder.addFormDataPart("user_id", session.getUserId());
            builder.addFormDataPart("fromUserId", session.getUserId());
            builder.addFormDataPart("priority", priority);
            builder.addFormDataPart("username", session.getEmail());
            builder.addFormDataPart("location", location1);
            builder.addFormDataPart("itemTitle", titleET.getText().toString());
            builder.addFormDataPart("itemDesc", descriptionET.getText().toString());
            builder.addFormDataPart("category", "" + cat);
            builder.addFormDataPart("subCategory", "" + subCat);
            builder.addFormDataPart("showComments", "" + comment);
            builder.addFormDataPart("showPhoneNumber", "" + phone);
            builder.addFormDataPart("showMessage", "" + message);
            builder.addFormDataPart("showWhatsapp", "" + whatsApp);
            builder.addFormDataPart("city", location1);
            builder.addFormDataPart("country", "المملكة العربية السعودية");
            builder.addFormDataPart("sex", sex);
            builder.addFormDataPart("passport", passport);
            if (adsImagesPath.size() == 0) {


                File coverImgFile = new File(getFilesDir(), "placeholder.png");
                builder.addFormDataPart("imgUrl", "placeholder.png", RequestBody.create(MediaType.parse("multipart/form-data"), coverImgFile));
            } else {
                File coverImgFile = Utils.prepareFilePart(AddAdActivity.this, adsImagesPath.get(0));
                builder.addFormDataPart("imgUrl", coverImgFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), coverImgFile));
                for (int i = 0; i < adsImagesPath.size(); i++) {
                    boolean upload = true;
                    File file = Utils.prepareFilePart(AddAdActivity.this, adsImagesPath.get(i));
                    for (String imageName : imagesName) {
                       if (file.getName().compareTo(imageName) == 0) {
                            upload = false;
                            break;
                        }
                    }
                   if (upload) {
                        builder.addFormDataPart("images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                    } else {
                    }

                }
      }
            if (videoPath != null) {
                File file = Utils.prepareFilePart(AddAdActivity.this, videoPath);
                builder.addFormDataPart("videoUrl", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

            MultipartBody requestBody = builder.build();


            Call<NoDataResponseModel> call = apiService.updatePost(requestBody);
            call.enqueue(new Callback<NoDataResponseModel>() {
                @Override
                public void onResponse(Call<NoDataResponseModel> call, Response<NoDataResponseModel> response) {
                    NoDataResponseModel responseModel = response.body();
                    Log.i("post_update", alreadyImageCount + " --- null ----" + (responseModel == null));
                    Log.i("post_update", alreadyImageCount + " --- null ----" + (response.message()));
                    Log.i("post_update", alreadyImageCount + " --- null ----" + (response.isSuccessful()));
                    if (responseModel != null) {
                        Log.i("post_update", alreadyImageCount + " --- isError" + responseModel.isError());
                        Log.i("post_update", alreadyImageCount + " --- getMessage" + responseModel.getMessage());
                    }
//                    Log.i("post_update",alreadyImageCount+" --- alreadyImageCount");
                    if (responseModel != null && !responseModel.isError()) {
                        progress_circular.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        // infoDialog("Server Error.");
                        progress_circular.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(Call<NoDataResponseModel> call, Throwable t) {
                    Log.i("post_update", alreadyImageCount + " --- alreadyImageCount");
                    t.printStackTrace();
                    String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    progress_circular.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getFilesDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } catch (Exception ex) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }




    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            // openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void forceRTLIfSupported() {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

            final Uri imageUri = data.getData();
            adsImagesPath.add(imageUri);
            adsImages.add(imageUri);
            showImages( );
        }

        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && null != data) {

            final Uri imageUri = data.getData();
            videoPath = imageUri;
            adsImages.add(videoPath);
            showImages( );
        }

        if (resultCode == RESULT_CANCELED) {
            return;
        }


    }

    private void showImages() {
//        addImagesLyt.setVisibility(View.GONE);
        adsImagesList.setVisibility(View.VISIBLE);
        adapterAdsImages = new AdapterAdsImages(adsImages, this, this, this, this,this);
        adsImagesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adsImagesList.setAdapter(adapterAdsImages);
        adsImagesList.smoothScrollToPosition(adsImages.size());
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {

        Log.i("oncreateCalled", v.getId() + " ----- " + R.id.adImagesContainer);
        switch (v.getId()) {
            case R.id.choiceItemImg1:
                tempImage = choiceItemImg1;
                isCoverImg = false;
                break;
            case R.id.choiceItemImg2:
                tempImage = choiceItemImg2;
                isCoverImg = false;
                break;
            case R.id.choiceItemImg3:
                tempImage = choiceItemImg3;
                isCoverImg = false;
                break;
            case R.id.choiceItemImg4:
                tempImage = choiceItemImg4;
                isCoverImg = false;
                break;
            case R.id.coverImg:
                tempImage = (SquareImageView) v;
                isCoverImg = true;
                break;
            case R.id.adImagesContainer:
                bottomSheetDialogue();
                break;

        }
    }

    private void selectAdImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    private void selectAdVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_VIDEO);
    }


    private void uploadFileAPI(File fileToSend) {

        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileToSend);
        MultipartBody.Part media = MultipartBody.Part.createFormData("images[]", FileUtils.getFileName(fileToSend), requestFile);


        apiService.uploadImage(
                "upload-image", session.getEmail(), FileUtils.getFileName(fileToSend), media
        ).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String model = response.body();
                if (model != null) {
                    Toast.makeText(getApplicationContext(), "Image Uploaded!", Toast.LENGTH_LONG).show();
                    progress_circular.setVisibility(View.GONE);
                    // session.setImage(model.getPath());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "" + t, Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void getSelectedImage(int position) {

    }


    String selectedCategory = "";
    String subCategory = "";

    private void fetchData() {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<GetPostResponseModel> call = apiService.getItem(Integer.parseInt(session.getUserId()), Integer.parseInt(editId));
        Log.i("oncreateCalled", call.request().url() + " --- " + session.getUserId() + " ----- " + editId);
        call.enqueue(new Callback<GetPostResponseModel>() {
            @Override
            public void onResponse(Call<GetPostResponseModel> call, Response<GetPostResponseModel> response) {
                GetPostResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {
                    progress_circular.setVisibility(View.GONE);

                    id = responseModel.getData().getId();
                    titleET.setText(responseModel.getData().getItemTitle());
                    descriptionET.setText(responseModel.getData().getItemDesc());
                    cat = Integer.parseInt(responseModel.getData().getCategory());
                    selectedCategory = responseModel.getData().getCategory();
                    subCategory = responseModel.getData().getSubCategory();
                    try {
                        vaccine.setText(responseModel.getData().getVaccine_detail());
                        age.setText(responseModel.getData().getAge());

                    } catch (Exception e) {
                    }

                    if (responseModel.getData().getPassport().equalsIgnoreCase("yes")) {
                        yes.performClick();
                    } else {
                        no.performClick();
                    }

                    if (responseModel.getData().getSex().equalsIgnoreCase("male")) {
                        male.performClick();
                    } else {
                        female.performClick();
                    }
                    Log.i("getSubCategory1233", subCategory + " ----- " + selectedCategory);


                    if (responseModel.getData().getShowPhoneNumber().contentEquals("1")) {
                        cb1.setChecked(true);
                    }

                    if (responseModel.getData().getShowMessage().contentEquals("1")) {
                        cb2.setChecked(true);
                    }

                    if (responseModel.getData().getShowComments().contentEquals("1")) {
                        cb3.setChecked(true);
                    }


                    Log.i("post_update", responseModel.getData().getrelatedAdImages().size() + " ----- size");
                    Log.i("post_update", responseModel.getData().getImages().size() + " ----- size");


                    if (responseModel.getData().getShowWhatsapp() != null && responseModel.getData().getShowWhatsapp().contentEquals("1")) {
                        cb4.setChecked(true);
                    }
                    alreadyImageCount = responseModel.getData().getImages().size();
                    imagesName = new ArrayList<>();
                    for (int i = 0; i < responseModel.getData().getImages().size(); i++) {

                        Log.i("post_update", responseModel.getData().getImages().get(i).getFileName() + " ----- size");
                        String url = responseModel.getData().getImages().get(i).getFileName();

                        String[] multiImage = url.split(",");
                        try {

                            for (String image : multiImage) {

                                if (image.length() > 0) {
                                    try {
                                        String imageName = responseModel.getData().getImages().get(i).getFileName().split("/")
                                                [responseModel.getData().getImages().get(i).getFileName().split("/").length - 1];
                                        imagesName.add(imageName);
                                        Log.i("post_update_url", imageName + " --- " + image);

                                        new DownloadFile(image, imageName).execute();
                                    } catch (Exception ex) {
                                    }
                                }
                            }

                        } catch (Exception ex) {
                        }
                    }
                    setCategorySpinner();
                }
            }

            @Override
            public void onFailure(Call<GetPostResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddAdActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    ArrayList<String> imagesName = new ArrayList<>();
    int alreadyImageCount = 0;

    private void deleteApi(String id) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<NoDataResponseModel> call = apiService.deleteItem(session.getUserId(), id);
        call.enqueue(new Callback<NoDataResponseModel>() {
            @Override
            public void onResponse(Call<NoDataResponseModel> call, Response<NoDataResponseModel> response) {
                NoDataResponseModel responseModel = response.body();
                progress_circular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();

                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<NoDataResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddAdActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    private void deleteImages(String id) {
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<NoDataResponseModel> call = apiService.deleteItemImage(id);
        call.enqueue(new Callback<NoDataResponseModel>() {
            @Override
            public void onResponse(Call<NoDataResponseModel> call, Response<NoDataResponseModel> response) {
                NoDataResponseModel responseModel = response.body();
                progress_circular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                if (adapterAdsImages != null) {
                    adapterAdsImages.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NoDataResponseModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddAdActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void clickToRemove(int position) {
//        imagesName.remove(position);
      if (adsImagesPath.size()>0&&position<adsImagesPath.size()){
          adsImagesPath.remove(position);
      }
        if (adsImages.size()>0&&position<adsImages.size()) {
            adsImages.remove(position);
        }
        adapterAdsImages.notifyDataSetChanged();
    }

    private class DownloadFile extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private String fileName;

        public DownloadFile(String url, String fileName) {
            this.url = url;
            this.fileName = fileName;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(url).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {

            if (result != null) {
                File dir = new File(getFilesDir(), "Images");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File destination = new File(dir, fileName);

                try {
                    destination.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    result.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(destination);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
//                    selectedFile = destination;
                    adsImages.add(Uri.fromFile(destination));
                    adsImagesPath.add(Uri.fromFile(destination));

                    showImages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void bottomSheetDialogue() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View bottomSheet = getLayoutInflater().inflate(R.layout.media_selection, null);
        TextView tvPicture = bottomSheet.findViewById(R.id.tvPicture);
        TextView tvVideoGallery = bottomSheet.findViewById(R.id.tvVideoGallery);
        tvVideoGallery.setVisibility(View.GONE);
        tvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAdImages();
                dialog.dismiss();
            }
        });
        tvVideoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAdVideo();
                dialog.dismiss();
            }
        });
        dialog.setContentView(bottomSheet);
        dialog.show();
    }
}