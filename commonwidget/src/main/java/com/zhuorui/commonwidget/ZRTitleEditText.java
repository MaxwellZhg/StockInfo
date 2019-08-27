package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zhuorui.securities.base2app.util.ResUtil;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-23 10:09
 * desc   :
 */
public class ZRTitleEditText extends FrameLayout implements View.OnFocusChangeListener, TextWatcher {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private TextView vTitle;
    private EditText vEt;
    private ImageView vRightIcon;
    //    private ImageView vRImg;
    private int mOrientation = -1;
    private Drawable mRightBtnDraw;


    public ZRTitleEditText(Context context) {
        this(context, null);
    }

    public ZRTitleEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRTitleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRTitleEditText);
        int orientation = a.getInt(R.styleable.ZRTitleEditText_zr_teditOrientation, VERTICAL);
        String title = a.getString(R.styleable.ZRTitleEditText_zr_teditTitle);
        String text = a.getString(R.styleable.ZRTitleEditText_zr_teditText);
        String hiht = a.getString(R.styleable.ZRTitleEditText_zr_teditHint);
        if (TextUtils.isEmpty(hiht)) {
            hiht = "请输入" + title;
        }
        setOrientation(orientation);
        setTitle(title);
        setText(text);
        setHint(hiht);

        setRightIcon(a);
        a.recycle();
    }

    private void setRightIcon(TypedArray a) {
        vRightIcon = findViewById(R.id.iv_right_icon);
        if (vRightIcon == null) return;
        int width = a.getDimensionPixelOffset(R.styleable.ZRTitleEditText_zr_iconWidth, 0);
        int hight = a.getDimensionPixelOffset(R.styleable.ZRTitleEditText_zr_iconHight, 0);
        int resId = a.getResourceId(R.styleable.ZRTitleEditText_zr_iconSrc, 0);
        int visible = a.getResourceId(R.styleable.ZRTitleEditText_zr_iconVisibility, 0);
        vRightIcon.setVisibility(visible);
        ViewGroup.LayoutParams params = vRightIcon.getLayoutParams();
//        params.width = ResUtil.INSTANCE.getDimensionDp2Px(width);
//        params.height = ResUtil.INSTANCE.getDimensionDp2Px(hight);
        params.width = width;
        params.height = hight;
        vRightIcon.setImageResource(resId);
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
        inflate(getContext(), orientation == VERTICAL ? R.layout.layout_title_edittext_vertical : R.layout.layout_title_edittext_horizontal, this);
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
