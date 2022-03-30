package com.zoolife.app.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.AllPost.AllPostResponseModel;
import com.zoolife.app.ResponseModel.AllPost.DataItem;
import com.zoolife.app.ResponseModel.Category.CategoryResponseModel;
import com.zoolife.app.ResponseModel.CityNameResponseModel.CityNameResponseModel;
import com.zoolife.app.ResponseModel.SearchPost.SearchResponseModel;
import com.zoolife.app.ResponseModel.SubCategory.SubCategoryResponseModel;
import com.zoolife.app.SortedPostActivity;
import com.zoolife.app.activity.AddAdActivity;
import com.zoolife.app.activity.FavouriteActivity;
import com.zoolife.app.activity.ImageDetailActivity;
import com.zoolife.app.activity.LoginActivity;
import com.zoolife.app.activity.SearchActivity;
import com.zoolife.app.adapter.AdSliderAdapter;
import com.zoolife.app.adapter.CategoryAdapter;
import com.zoolife.app.adapter.HomeAdapter;
import com.zoolife.app.adapter.NewSubCategoryAdapter;
import com.zoolife.app.adapter.SearchCatItemAdapter;
import com.zoolife.app.adapter.SearchCategoryAdapter;
import com.zoolife.app.adapter.SubCategoryAdapter;
import com.zoolife.app.models.CategoryModel;
import com.zoolife.app.models.HomeModel;
import com.zoolife.app.models.ImageData;
import com.zoolife.app.models.SliderModel;
import com.zoolife.app.models.SubCategoryModel;
import com.zoolife.app.models.related_ad_home.Datum;
import com.zoolife.app.models.related_ad_home.RelatedAdHomeModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;
import com.zoolife.app.utility.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.zoolife.app.activity.AppBaseActivity.categories;
import static com.zoolife.app.activity.AppBaseActivity.session;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchCategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCategoriesFragment extends Fragment {

    ImageView btnSearchBack;
    RelativeLayout dropdown_btn;
    ProgressBar progress_circular;


    String[] cities = new String[]{};
    String[] mainCategory;
    String[] subCategory;

    SearchCatItemAdapter homeAdapter;
    RecyclerView recyclerView;

    List<HomeModel> dataList;
    List<HomeModel> dataListFiltered;
    private EditText etSearch;
    private LinearLayout llSpinner;
    private RecyclerView searchCategoryRv;
    private SearchCategoryAdapter searchCategoryAdapter;
    private Spinner spinner;


    public SearchCategoriesFragment() {
        // Required empty public constructor
    }


    public static SearchCategoriesFragment newInstance() {
        SearchCategoriesFragment fragment = new SearchCategoriesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        View view = inflater.inflate(R.layout.fragment_search, container, false);
        dropdown_btn = view.findViewById(R.id.dropdown_btn);
        llSpinner = view.findViewById(R.id.ll_spinners);
        recyclerView = view.findViewById(R.id.search_recyclerview);
        progress_circular = view.findViewById(R.id.search_pd);
        etSearch = view.findViewById(R.id.et_search);
        spinner = (Spinner) view.findViewById(R.id.spinnerCity);

        searchCategoryRv=view.findViewById(R.id.searchCategoryRv);

//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset);
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(1, spacingInPixels, true, 0);
//        recyclerView.addItemDecoration(itemDecoration);
        getAllCityNames();
        getCategory();

        getAllPost();

        dataListFiltered = new ArrayList<>();
        dataListFiltered.clear();
        etSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0) {
                    llSpinner.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

//                    dataListFiltered = dataList.forEach(articlesModel -> );
                    if (dataList.size() > 0) {
                        dataListFiltered.clear();
                        for (int m = 0; m < dataList.size(); m++) {
                            if (dataList.get(m).title.toLowerCase().contains(s.toString().toLowerCase())) {
                                dataListFiltered.add(dataList.get(m));
                            }
                        }
                        if (dataListFiltered.size() > 0) {
//                            homeAdapter = new SearchCatItemAdapter(SearchActivity.this, dataListFiltered,session);
//                            recyclerView.setAdapter(homeAdapter);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                            homeAdapter.SetData(dataListFiltered);
//                            homeAdapter.notifyDataSetChanged();


                        } else {
//                            homeAdapter = new SearchCatItemAdapter(SearchActivity.this, dataList,session);
//                            recyclerView.setAdapter(homeAdapter);
//                            recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2, LinearLayoutManager.VERTICAL, false));
//
//                            homeAdapter.notifyDataSetChanged();

                            homeAdapter.SetData(dataListFiltered);

//                            articlesAdapter.notifyDataSetChanged();
//                        Toast.makeText(this,"Nothing found",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        dataListFiltered.clear();
                        homeAdapter.SetData(dataListFiltered);

                    }
//                    field2.setText("");
                } else {
                    dataListFiltered.clear();
//                    homeAdapter = new SearchCatItemAdapter(SearchActivity.this, dataList,session);
//                    recyclerView.setAdapter(homeAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
//                    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset);
//                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(2, spacingInPixels, true, 0);
//                    recyclerView.addItemDecoration(itemDecoration);

//                    homeAdapter.notifyDataSetChanged();
                    homeAdapter.SetData(dataListFiltered);

                    llSpinner.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        btnSearchBack = (ImageView) view.findViewById(R.id.btnSearchBack);


        return view;
    }

    private void getAllCityNames() {

        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<CityNameResponseModel> call = apiService.getAllCityNames();
        call.enqueue(new Callback<CityNameResponseModel>() {
            @Override
            public void onResponse(Call<CityNameResponseModel> call, Response<CityNameResponseModel> response) {
                try {
                    CityNameResponseModel responseModel = response.body();
                    cities = new String[responseModel.getData().size()];
                    for (int i = 0; i < responseModel.getData().size(); i++) {
                        cities[i] = responseModel.getData().get(i).getName();
                    }

                    List<String> wordList = new ArrayList<String>(Arrays.asList(cities));

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, wordList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<CityNameResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSubCategory(int cat_id) {

        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<SubCategoryResponseModel> call = apiService.getSubCategory(cat_id);
        call.enqueue(new Callback<SubCategoryResponseModel>() {
            @Override
            public void onResponse(Call<SubCategoryResponseModel> call, Response<SubCategoryResponseModel> response) {
                SubCategoryResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {

                    ArrayList<SubCategoryModel> arrayList = new ArrayList<>();
                    subCategory = new String[responseModel.getData().size() + 1];
                    subCategory[0] = "حدد فئة فرعية";

                    if (responseModel.getData() != null) {
                        for (int i = 0; i < responseModel.getData().size(); i++) {
                            arrayList.add(new SubCategoryModel(responseModel.getData().get(i).getTitle(), responseModel.getData().get(i).getId()));
                            subCategory[i + 1] = responseModel.getData().get(i).getTitle();
                        }

                        //Creating the ArrayAdapter instance having the country list
                        ArrayAdapter aa3 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, subCategory);
                        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "No Record Found!", Toast.LENGTH_LONG).show();
                    }


                }

            }

            @Override
            public void onFailure(Call<SubCategoryResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getCategory() {

//        ApiService apiService = ApiClient.getClientZoo().create(ApiService.class);
        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<CategoryResponseModel> call = apiService.getCategory();

        if(Locale.getDefault().getDisplayLanguage() == "en") {
            call.request().newBuilder().addHeader("X-Localization", "en");
        }
        Log.i("getHeaderEnglish","---"+ Locale.getDefault().getDisplayLanguage());
        Log.i("getHeaderEnglish","---"+call.request().url());
//        call.request()
        call.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {
                try {

                    CategoryResponseModel responseModel = response.body();
                    categories = response.body();
                    if (responseModel != null && !responseModel.isError()) {

                        ArrayList<CategoryModel> arrayList = new ArrayList<>();

                        mainCategory = new String[responseModel.getData().size() + 1];
//                    mainCategory[0] = "اختر الفئة";

                        for (int i = 0; i < responseModel.getData().size(); i++) {

                            com.zoolife.app.ResponseModel.Category.DataItem categoryModel = responseModel.getData().get(i);
                            arrayList.add(new CategoryModel(categoryModel.getTitle(), categoryModel.getImgUnSelected(), String.valueOf(categoryModel.getId())));
                            mainCategory[i + 1] = categoryModel.getTitle();

                        }

                        //Creating the ArrayAdapter instance having the country list
                        ArrayAdapter aa2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mainCategory);
                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner


                        searchCategoryAdapter = new SearchCategoryAdapter(getActivity(), arrayList, SearchCategoriesFragment.this::onItemClick);

                        searchCategoryRv.setLayoutManager(new LinearLayoutManager(getActivity()));

                        searchCategoryRv.setAdapter(searchCategoryAdapter);


//                    Collections.reverse(arrayList);


//                    categoryAdapter = new CategoryAdapter(getActivity(),arrayList, HomeFragment.this);
//                    category_rv.setAdapter(categoryAdapter);
//                    category_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));


                    } else {
                        // infoDialog("Server Error.");
//                    progress_circular.setVisibility(View.GONE);
                    }
                }
                catch (Exception ex)
                {}

            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//                progress_circular.setVisibility(View.GONE);
            }

        });
    }
    public void onItemClick(CategoryModel categoryModel) {

        int cat_id=Integer.parseInt(categoryModel.id);



        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<AllPostResponseModel> call = apiService.getAllItembyCategory(cat_id, session.getCity(), 1, 0);
        call.enqueue(new Callback<AllPostResponseModel>() {
            @Override
            public void onResponse(Call<AllPostResponseModel> call, Response<AllPostResponseModel> response) {
                AllPostResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {

                    progress_circular.setVisibility(View.GONE);
                    llSpinner.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    ArrayList<HomeModel> arrayList = new ArrayList<>();

                    for (int i = 0; i < responseModel.getData().size(); i++) {
                        if (responseModel.getData().get(i).getCategory() == cat_id) {
                            DataItem dataItem = responseModel.getData().get(i);
                            arrayList.add(new HomeModel(dataItem.getItemTitle(), dataItem.getCreatedAt(), dataItem.getCity(), dataItem.getUsername(), dataItem.getImgUrl(), String.valueOf(dataItem.getId()), dataItem.getPriority()));
                        }
                    }

                    if (arrayList.size() > 0) {
                        homeAdapter = new SearchCatItemAdapter(getActivity(), arrayList,session);
                        recyclerView.setAdapter(homeAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        homeAdapter.notifyDataSetChanged();
                        Log.i("askjdvweavd",":dffww");
                    } else {
                        Log.i("askjdvweavd",":ppp");
                        homeAdapter = new SearchCatItemAdapter(getActivity(), new ArrayList<>(),session);
                        recyclerView.setAdapter(homeAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        homeAdapter.notifyDataSetChanged();

                    }

                } else {
                    // infoDialog("Server Error.");

                    recyclerView.removeAllViews();
                    Toast.makeText(getActivity(), "No Ad for this category", Toast.LENGTH_SHORT).show();
                    progress_circular.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<AllPostResponseModel> call, Throwable t) {
                t.printStackTrace();
//                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });

    }


    String nextPost = "";
    boolean firstTime = true;

    public void getAllPost() {
//        progress_circular.setVisibility(View.VISIBLE);
        if(homeAdapter == null) {
            homeAdapter = new SearchCatItemAdapter(getActivity(), new ArrayList<>(), session);
            recyclerView.setAdapter(homeAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<AllPostResponseModel> call = apiService.getAllPost();

        if(!firstTime)
        {
            if(nextPost == null ||nextPost.length() == 0)
            {
                return;
            }
            call = apiService.getAllPostNew(nextPost);
        }
        call.enqueue(new Callback<AllPostResponseModel>() {
            @Override
            public void onResponse(Call<AllPostResponseModel> call, Response<AllPostResponseModel> response) {

                try {

                    AllPostResponseModel responseModel = response.body();
                    if (responseModel != null && !responseModel.isError()) {

                        Log.d("SEARCH_TAG", "getallpost: " + response.body().getData().toString());
//                        progress_circular.setVisibility(View.GONE);

                        ArrayList<HomeModel> arrayList = new ArrayList<>();

                        nextPost = responseModel.getNextPage();
                        for (int i = 0; i < responseModel.getData().size(); i++) {
                            DataItem dataItem = responseModel.getData().get(i);
                            arrayList.add(new HomeModel(dataItem.getItemTitle(), dataItem.getCreatedAt(), dataItem.getCity(), dataItem.getUsername(), dataItem.getImgUrl(), String.valueOf(dataItem.getId()), dataItem.getPriority()));
                        }

                        if (arrayList.size() > 0) {
                            dataList = arrayList;
                            if(homeAdapter == null) {
                                homeAdapter = new SearchCatItemAdapter(getActivity(), new ArrayList<>(), session);
                                recyclerView.setAdapter(homeAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(
                                        getActivity(), LinearLayoutManager.VERTICAL, false));
                            }
//                            else{
//                                homeAdapter.UpdateData(arrayList);
//                            }
                        }

                    } else {
                        // infoDialog("Server Error.");
                        progress_circular.setVisibility(View.GONE);
                    }
                    getAllPost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AllPostResponseModel> call, Throwable t) {
                t.printStackTrace();
//                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
//                Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                progress_circular.setVisibility(View.GONE);
            }
        });
    }



}