package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-23 15:34
 * desc   :
 */
public class ZRUploadImageView extends FrameLayout implements View.OnClickListener {

    private TextView vTitle;
    private TextView vBtnText;
    private View vBtn;
    private ImageView vImg;
    private String mBtnText;
    private Drawable mPlaceholder;
    private Drawable mWatermark;

    public ZRUploadImageView(Context context) {
        this(context, null);
    }

    public ZRUploadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRUploadImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.layout_uplod_image_view, this);
        initView();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRUploadImageView);
        String title = a.getString(R.styleable.ZRUploadImageView_zr_upimageviewTitle);
        mBtnText = a.getString(R.styleable.ZRUploadImageView_zr_upimageviewBtnText);
        mPlaceholder = a.getDrawable(R.styleable.ZRUploadImageView_zr_upimageviewPlaceholder);
        mWatermark = a.getDrawable(R.styleable.ZRUploadImageView_zr_upimageviewWatermark);
        setTitle(title);
        setButtonText(mBtnText);
        setPlaceholder(mPlaceholder);
    }

    private void initView() {
        vTitle = findViewById(R.id.tv_title);
        vBtnText = findViewById(R.id.tv_btn_text);
        vBtn = findViewById(R.id.sb_btn);
        vImg = findViewById(R.id.iv_image);
        vBtn.setOnClickListener(this);
    }

    private void setPlaceholder(Drawable drawable) {
        vImg.setImageDrawable(drawable);
    }

    private void setButtonText(String mBtnText) {
        vBtnText.setText(mBtnText);
    }

    private void setTitle(String title) {
        vTitle.setText(title);
    }

    @Override
    public void onClick(View view) {
        if (view == vBtn){

        }
    }
}
