package com.zoolife.app.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoolife.app.BuildConfig;
import com.zoolife.app.R;
import com.zoolife.app.activity.AboutUsActivity;
import com.zoolife.app.activity.AddAdActivity;
import com.zoolife.app.activity.AddAuctionActivity;
import com.zoolife.app.activity.DeliveryOrderActivity;
import com.zoolife.app.activity.LoginActivity;
import com.zoolife.app.activity.MyAuctionActivity;
import com.zoolife.app.activity.MyFavouritesActivity;
import com.zoolife.app.activity.MyPostsActivity;
import com.zoolife.app.activity.SplashActivity;
import com.zoolife.app.adapter.PDFViewActivity;
import com.zoolife.app.utility.LocaleHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import static com.zoolife.app.activity.AppBaseActivity.session;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMoreFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TextView logoutTV;
    TextView version;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView languageSwitch;
    ImageView imgBack;

    public ViewMoreFragment() {
        // Required empty public constructor
    }

    RelativeLayout searchLayout, loginLayout, usernameLayout, addAdLayout, myAuctionLayout,myAdsLayout, ordervetLayout, myAccountLayout,addAuctionLayout;
    RelativeLayout orderLayout, favouriteLayout, tNcLayout, bannedAdsLayout, callUsLayout, changeCityLayout, logoutLayout, back;

    public static ViewMoreFragment newInstance(String param1, String param2) {
        ViewMoreFragment fragment = new ViewMoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_more, container, false);

        languageSwitch = view.findViewById(R.id.lang_toggle);
        String lang = LocaleHelper.getLanguage(getContext());
        if (lang.equals("ar")) {
            languageSwitch.setImageResource(R.drawable.eng_to_arb);
            LocaleHelper.setLocale(getActivity(), "ar");
        } else {
            languageSwitch.setImageResource(R.drawable.arb_to_eng);
            LocaleHelper.setLocale(getActivity(), "en");
        }
        imgBack = view.findViewById(R.id.imgBack);
        addAdLayout = view.findViewById(R.id.addAdLayout);
        favouriteLayout = view.findViewById(R.id.favouriteLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);
        addAuctionLayout = view.findViewById(R.id.addAuctionLayout);
        myAdsLayout = view.findViewById(R.id.myAdsLayout);
        myAuctionLayout = view.findViewById(R.id.myAuctionLayout);
        logoutTV = view.findViewById(R.id.logoutTV);
        version = view.findViewById(R.id.version);
        tNcLayout = view.findViewById(R.id.tNcLayout);
        bannedAdsLayout = view.findViewById(R.id.bannedAdsLayout);
        callUsLayout = view.findViewById(R.id.callUsLayout);
//        ordervetLayout = view.findViewById(R.id.ordervetLayout);
//        myAccountLayout = view.findViewById(R.id.myAccountLayout);
//        changeCityLayout = view.findViewById(R.id.changeCityLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        back = view.findViewById(R.id.back);
        version.setText(getText(R.string.version)+ BuildConfig.VERSION_NAME);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        callUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));


            }
        });


        if (!session.isLogin()) {
            logoutTV.setText("تسجيل الدخول");
        }

        tNcLayout.setOnClickListener(this);
        bannedAdsLayout.setOnClickListener(this);


//        ordervetLayout.setOnClickListener(this);
//        myAccountLayout.setOnClickListener(this);
//        changeCityLayout.setOnClickListener(this);
        orderLayout.setOnClickListener(this);
        favouriteLayout.setOnClickListener(this);


        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setIsLogin(false);
                SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                preferences.edit().putInt("notificationCount", 0).apply();
                preferences.edit().putInt("notificationCountN", 0).apply();

//                if(getActivity() instanceof )
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });

        addAdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLogin()) {
                    Intent intent = new Intent(getActivity(), AddAdActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        addAuctionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLogin()) {
                    Intent intent = new Intent(getActivity(), AddAuctionActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        myAdsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLogin())
                    startActivity(new Intent(getActivity(), MyPostsActivity.class));
                else
                    startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        myAuctionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLogin())
                    startActivity(new Intent(getActivity(), MyAuctionActivity.class));
                else
                    startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        languageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = LocaleHelper.getLanguage(getContext());
                if (lang.equals("ar")) {
                    languageSwitch.setImageResource(R.drawable.eng_to_arb);
                    LocaleHelper.setLocale(getActivity(), "en");
                } else {
                    languageSwitch.setImageResource(R.drawable.arb_to_eng);
                    LocaleHelper.setLocale(getActivity(), "ar");
                }
                getActivity().finish();
                Intent intent = new Intent(getContext(), SplashActivity.class);
                startActivity(intent);
            }
        });
//        languageSwitch.setOnClickListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
//            @Override
//            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
//                if (position == 0) {
//                    LocaleHelper.setLocale(getActivity(), "ar");
//                } else {
//                    LocaleHelper.setLocale(getActivity(), "en");
//                }
//                getActivity().finish();
//                Intent intent = new Intent(getContext(), SplashActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }


    public void showAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("قريبا!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tNcLayout:
                Intent intent = new Intent(getActivity(), PDFViewActivity.class);
                intent.putExtra("file", "terms_and_policy_" + LocaleHelper.getLanguage(getActivity()));
                intent.putExtra("title", "Terms And Policy");
                getActivity().startActivity(intent);
                break;
            case R.id.bannedAdsLayout:
                Intent intent1 = new Intent(getActivity(), PDFViewActivity.class);
                intent1.putExtra("file", "banned_" + LocaleHelper.getLanguage(getActivity()));
                intent1.putExtra("title", "Banned");
                getActivity().startActivity(intent1);
                break;
            case R.id.favouriteLayout:
                if (session.isLogin()) {
                    Intent favIntent = new Intent(getActivity(), MyFavouritesActivity.class);
                    startActivity(favIntent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.orderLayout:
                if(session.isLogin()) {
                Intent favIntent = new Intent(getActivity(), DeliveryOrderActivity.class);
                startActivity(favIntent);
                }else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
//            case R.id.myAccountLayout:
//                Fragment fragment = new MissingActivity();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
//
//                break;
//            case R.id.ordervetLayout:
//
//            case R.id.changeCityLayout:
//                showAlert();
//                break;
        }
    }
}