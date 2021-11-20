package com.example.myRefri02;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

public class ProgressDialog extends Dialog
{
    public ProgressDialog(Context con)
    {
        super((Context) con);
        // 다이얼 로그 제목을 안보이게...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
    }
}