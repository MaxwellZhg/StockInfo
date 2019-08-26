package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-23 10:09
 * desc   :
 */
public class ZRTitleTextView extends FrameLayout implements View.OnFocusChangeListener, TextWatcher {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private TextView vTitle;
    private TextView vEt;
    //    private ImageView vRImg;
    private int mOrientation = -1;
    private Drawable mRightBtnDraw;


    public ZRTitleTextView(Context context) {
        this(context, null);
    }

    public ZRTitleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRTitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRTitleTextView);
//        mRightBtnDraw = a.getDrawable(R.styleable.ZhuoruiEditText_zret_right_button_resources);
        int orientation = a.getInt(R.styleable.ZRTitleTextView_zr_ttextviewOrientation, VERTICAL);
        String title = a.getString(R.styleable.ZRTitleTextView_zr_ttextviewTitle);
        String text = a.getString(R.styleable.ZRTitleTextView_zr_ttextviewText);
        String hiht = a.getString(R.styleable.ZRTitleTextView_zr_ttextviewHint);
        if (TextUtils.isEmpty(hiht)){
            hiht = "请选择" + title;
        }
        setOrientation(orientation);
        setTitle(title);
        setText(text);
        setHint(hiht);

    }

    public void setHint(String hiht) {
        vEt.setHint(hiht);
    }

    public void setText(String text) {
        vEt.setText(text);
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }

    public void setOrientation(int orientation) {
        if (orientation == mOrientation) return;
        mOrientation = orientation;
        removeAllViews();
        inflate(getContext(), orientation == VERTICAL ? R.layout.layout_title_textview_vertical : R.layout.layout_title_textview_horizontal, this);
        String title = vTitle != null ? vTitle.getText().toString() : "";
        String text = vEt != null ? vEt.getText().toString() : "";
        String hint = vEt != null ? vEt.getHint().toString() : "";
        vTitle = findViewById(R.id.tv_title);
        vEt = findViewById(R.id.et_edittext);
//        vRImg = findViewById(R.id.iv_image);
        vEt.setOnFocusChangeListener(this);
        vEt.addTextChangedListener(this);
        vEt.setText(title);
        vEt.setHint(hint);
        vEt.setText(text);
        vTitle.setText(title);
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
