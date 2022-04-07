package com.zoolife.app.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zoolife.app.R;

public class CustomDialogConnectionNeeded {
    private Dialog dialog;
    private TextView btnOk;

    public CustomDialogConnectionNeeded(Context context) {
        dialog = new Dialog(context);
//        dialog = new Dialog(context, R.style.RoundAlertDialog);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * (90 / 100f));
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialoge);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(screenWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initViews();
    }

    private void initViews() {
        btnOk = dialog.findViewById(R.id.btnCancel);
    }

    public void setCloseListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismissDialog();
            }
        });
    }

    public void showDialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
