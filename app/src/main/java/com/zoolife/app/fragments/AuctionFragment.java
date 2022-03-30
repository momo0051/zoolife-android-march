package com.zoolife.app.fragments;

import static com.zoolife.app.activity.AppBaseActivity.session;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zoolife.app.R;
import com.zoolife.app.ResponseModel.AllPost.AllPostResponseModel;
import com.zoolife.app.ResponseModel.AllPost.DataItem;
import com.zoolife.app.ResponseModel.Category.CategoryResponseModel;
import com.zoolife.app.ResponseModel.CityNameResponseModel.CityNameResponseModel;

import com.zoolife.app.activity.AddAuctionActivity;
import com.zoolife.app.adapter.AuctionAdapter;
import com.zoolife.app.adapter.SearchCategoryAdapter;
import com.zoolife.app.models.AuctionModel;
import com.zoolife.app.models.CategoryModel;
import com.zoolife.app.models.HomeModel;
import com.zoolife.app.network.ApiClient;
import com.zoolife.app.network.ApiService;
import com.zoolife.app.utility.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuctionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuctionFragment extends Fragment {

    private static final String TAG = "AuctionFragment";

    ImageView btnSearchBack;
    RelativeLayout dropdown_btn;
    ProgressBar progress_circular;


    String[] cities = new String[]{};
    String[] mainCategory;
    String[] subCategory;

    AuctionAdapter auctionAdapter;
//    RecyclerView recyclerView;

    ArrayList<AuctionModel.MData> dataList;
    ArrayList<AuctionModel.MData> dataListFiltered;
    private EditText etSearch;
    //    private LinearLayout llSpinner;
    private RecyclerView searchCategoryRv;
    private SearchCategoryAdapter searchCategoryAdapter;
    private Spinner spinnerCity, spinnerCategory;
    private RecyclerView auctionRecyclerView;
    static public CategoryResponseModel categories;
    ArrayList<String> categoryArraylist;


    public AuctionFragment() {
        // Required empty public constructor
    }


    public static AuctionFragment newInstance() {
        AuctionFragment fragment = new AuctionFragment();
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


        View view = inflater.inflate(R.layout.fragment_auction, container, false);
        auctionRecyclerView = view.findViewById(R.id.auction_rv);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_offset);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(2, spacingInPixels, true, 0);
        auctionRecyclerView.addItemDecoration(itemDecoration);
//        auctionRecyclerView.setVisibility(View.GONE);
        progress_circular = (ProgressBar) view.findViewById(R.id.progress_circular);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);


        getAllCityNames();


//        getSubCategory(128);

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
                    if (dataList != null && dataList.size() > 0) {
                        dataListFiltered.clear();
                        for (int m = 0; m < dataList.size(); m++) {
                            if (dataList.get(m).getItemTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                                dataListFiltered.add(dataList.get(m));
                            }
                        }
                        auctionAdapter.SetData(dataListFiltered);
                    } else {
                        dataListFiltered.clear();
                        auctionAdapter.SetData(dataListFiltered);

                    }
                } else {
                    dataListFiltered.clear();
                    auctionAdapter.SetData(dataList);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        btnSearchBack = (ImageView) view.findViewById(R.id.btnSearchBack);
        view.findViewById(R.id.add_auction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AddAuctionActivity.class);
                intent.putExtra("from", "Home");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        auctionAdapter = new AuctionAdapter(getActivity(), dataListFiltered, session);
        auctionRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        auctionRecyclerView.setAdapter(auctionAdapter);
        return view;
    }

    private void getAllCityNames() {
        progress_circular.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<CityNameResponseModel> call = apiService.getAllCityNames();
        call.enqueue(new Callback<CityNameResponseModel>() {
            @Override
            public void onResponse(Call<CityNameResponseModel> call, Response<CityNameResponseModel> response) {
                CityNameResponseModel responseModel = response.body();
                cities = new String[responseModel.getData().size()];
                for (int i = 0; i < responseModel.getData().size(); i++) {
                    cities[i] = responseModel.getData().get(i).getName();
                }

                List<String> wordList = new ArrayList<String>(Arrays.asList(cities));
                wordList.add(0,  getString(R.string.city));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, wordList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(dataAdapter);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (dataList != null && dataList.size() > 0) {
                            dataListFiltered.clear();
                            for (int m = 0; m < dataList.size(); m++) {
                                if (dataList.get(m).getCity().toLowerCase().contains(wordList.get(i).toString().toLowerCase())) {
                                    dataListFiltered.add(dataList.get(m));
                                }
                                if (m % 5 == 0) {
                                    AuctionModel.MData obj = new AuctionModel.MData();
                                    obj.setViewType(1);
                                    dataListFiltered.add(obj);
                                }
                            }
                            auctionAdapter.SetData(dataListFiltered);
                        } else {
                            dataListFiltered.clear();
                            if (auctionAdapter != null)
                                auctionAdapter.SetData(dataListFiltered);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                getCategory();
            }

            @Override
            public void onFailure(Call<CityNameResponseModel> call, Throwable t) {
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

        if (Locale.getDefault().getDisplayLanguage() == "en") {
            call.request().newBuilder().addHeader("X-Localization", "en");
        }
        Log.i("getHeaderEnglish", "---" + Locale.getDefault().getDisplayLanguage());
        Log.i("getHeaderEnglish", "---" + call.request().url());
//        call.request()
        call.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {
                try {

                    CategoryResponseModel responseModel = response.body();
                    categories = response.body();
                    if (responseModel != null && !responseModel.isError()) {

                        ArrayList<CategoryModel> arrayList = new ArrayList<>();

//                        mainCategory = new String[responseModel.getData().size() + 1];
//                    mainCategory[0] = "اختر الفئة";
                        categoryArraylist = new ArrayList<>();
                        for (int i = 0; i < responseModel.getData().size(); i++) {
                            if (responseModel.getData().get(i).getTitle() != null) {
                                com.zoolife.app.ResponseModel.Category.DataItem categoryModel = responseModel.getData().get(i);
                                arrayList.add(new CategoryModel(categoryModel.getTitle(), categoryModel.getImgUnSelected(), String.valueOf(categoryModel.getId())));
                                categoryArraylist.add(categoryModel.getTitle());
                            }

                        }

//                        ArrayList<String> categoryArray = new ArrayList<String>(Arrays.asList(mainCategory));

                        categoryArraylist.add(0, getString(R.string.category));
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categoryArraylist);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategory.setAdapter(dataAdapter);
                        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (dataList != null && dataList.size() > 0) {
                                    dataListFiltered.clear();
                                    for (int m = 0; m < dataList.size(); m++) {
                                        if ((dataList.get(m).getCategory() + "").equals(arrayList.get(i).getId())) {
                                            dataListFiltered.add(dataList.get(m));
                                        }
                                        if (m % 5 == 0) {
                                            AuctionModel.MData obj = new AuctionModel.MData();
                                            obj.setViewType(1);
                                            dataListFiltered.add(obj);
                                        }
                                    }
                                    auctionAdapter.SetData(dataListFiltered);
                                } else {
                                    dataListFiltered.clear();
                                    if (auctionAdapter != null)
                                        auctionAdapter.SetData(dataListFiltered);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
//                        CategorySpinnerAdapter dataAdapter = new CategorySpinnerAdapter(getActivity(), categoryArray);
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerCategory.setAdapter(dataAdapter);

                        //Creating the ArrayAdapter instance having the country list
//                        ArrayAdapter aa2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mainCategory);
//                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        //Setting the ArrayAdapter data on the Spinner
//                        List<String> categoryArray = new ArrayList<String>(Arrays.asList(mainCategory));
//                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
//                                (getActivity(), android.R.layout.simple_spinner_item, categoryArray);
//                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinnerCategory.setAdapter(dataAdapter);

//                        searchCategoryAdapter = new SearchCategoryAdapter(getActivity(), arrayList, AuctionFragment.this::onItemClick);
//
//                        searchCategoryRv.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                        searchCategoryRv.setAdapter(searchCategoryAdapter);


//                    Collections.reverse(arrayList);


//                    categoryAdapter = new CategoryAdapter(getActivity(),arrayList, HomeFragment.this);
//                    category_rv.setAdapter(categoryAdapter);
//                    category_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));


                    } else {
                        // infoDialog("Server Error.");
//                    progress_circular.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                }

                getAllPost();
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
        int cat_id = Integer.parseInt(categoryModel.id);
        progress_circular.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<AllPostResponseModel> call = apiService.getAllItembyCategory(cat_id, session.getCity(), 1, 0);
        call.enqueue(new Callback<AllPostResponseModel>() {
            @Override
            public void onResponse(Call<AllPostResponseModel> call, Response<AllPostResponseModel> response) {
                AllPostResponseModel responseModel = response.body();
                if (responseModel != null && !responseModel.isError()) {

                    progress_circular.setVisibility(View.GONE);
//                    llSpinner.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);

                    ArrayList<HomeModel> arrayList = new ArrayList<>();

                    for (int i = 0; i < responseModel.getData().size(); i++) {
                        if (responseModel.getData().get(i).getCategory() == cat_id) {
                            DataItem dataItem = responseModel.getData().get(i);
                            arrayList.add(new HomeModel(dataItem.getItemTitle(), dataItem.getCreatedAt(), dataItem.getCity(), dataItem.getUsername(), dataItem.getImgUrl(), String.valueOf(dataItem.getId()), dataItem.getPriority()));
                        }
                    }

//                    if (arrayList.size() > 0) {
//                        AuctionAdapter = new AuctionAdapter(getActivity(), arrayList,session);
////                        recyclerView.setAdapter(AuctionAdapter);
////                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        AuctionAdapter.notifyDataSetChanged();
//                        Log.i("askjdvweavd",":dffww");
//                    } else {
//                        Log.i("askjdvweavd",":ppp");
////                        AuctionAdapter = new SearchCatItemAdapter(getActivity(), new ArrayList<>(),session);
////                        recyclerView.setAdapter(AuctionAdapter);
////                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        AuctionAdapter = new AuctionAdapter(getActivity(), arrayList, session);
//                        AuctionAdapter.notifyDataSetChanged();
//
//                    }

                } else {
                    // infoDialog("Server Error.");

//                    recyclerView.removeAllViews();
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
        progress_circular.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getActivity()).create(ApiService.class);
        Call<AuctionModel> call = apiService.getAuctionList();
//        call.request().
//        call.request().
        Log.i("getAllPost", call.request().url() + " --- allpost");
        call.enqueue(new Callback<AuctionModel>() {
            @Override
            public void onResponse(Call<AuctionModel> call, Response<AuctionModel> response) {
                Log.i("getAllPost", response.toString() + " --- response");
                AuctionModel responseModel = response.body();

                if (responseModel != null && !responseModel.getError()) {

                    Log.d(TAG, "getallpost: " + response.body().getData().toString());
                    progress_circular.setVisibility(View.GONE);

                    dataList = (ArrayList<AuctionModel.MData>) responseModel.getData().getData();
                    setData();

                } else {
                    // infoDialog("Server Error.");
                    progress_circular.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<AuctionModel> call, Throwable t) {
                Log.i("getAllPost", " --- onFailure");
                t.printStackTrace();
                String strr = t.getMessage() != null ? t.getMessage() : "Error in server";
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                progress_circular.setVisibility(View.GONE);
            }
        });
    }

    private void setData() {
        ArrayList<AuctionModel.MData> videoArray = new ArrayList<>();
        ArrayList<AuctionModel.MData> imageArray = new ArrayList<>();
        ArrayList<AuctionModel.MData> auctionArray = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            AuctionModel.MData dataItem = dataList.get(i);
            if (dataItem.getVideoUrl() != null && !dataItem.getVideoUrl().isEmpty()) {
                videoArray.add(dataItem);
            } else {
                imageArray.add(dataItem);
            }
        }
        auctionArray.addAll(videoArray);
        auctionArray.addAll(imageArray);

        int size = auctionArray.size();
        int addInt = auctionArray.size() / 6;
        /*bellow code is written for admob*/
        for (int i = 0; i < size + addInt; i++) {
            if (i > 0 && i % 6 == 0) {
                AuctionModel.MData obj = new AuctionModel.MData();
                obj.setViewType(1);
                auctionArray.add(i, obj);
            }
        }
        if (auctionArray != null && auctionArray.size() > 0) {
            auctionAdapter = new AuctionAdapter(getActivity(), auctionArray, session);
//            auctionRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
            GridLayoutManager manager=new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return auctionArray.get(position).getViewType() == 1 ? 2 : 1;
//         return position%6==0 && position>0?2:1;
                }
            });
            auctionRecyclerView.setLayoutManager(manager);
            auctionRecyclerView.setAdapter(auctionAdapter);
        }
    }


}