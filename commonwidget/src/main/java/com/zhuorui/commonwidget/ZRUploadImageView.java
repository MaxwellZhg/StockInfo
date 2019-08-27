package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zhuorui.commonwidget.dialog.GetPicturesModeDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-08-23 15:34
 * desc   :
 */
public class ZRUploadImageView extends FrameLayout implements View.OnClickListener, GetPicturesModeDialog.OnGetPicturesModeListener {

    private TextView vTitle;
    private TextView vBtnText;
    private View vBtn;
    private ImageView vImg;
    private String mBtnText;
    private Drawable mPlaceholder;
    private String mWatermarkTxt;
    private GetPicturesModeDialog dialog;
    private OnUploadImageListener mListener;
    private String mFileName;
    private int mAlbumRequestCode = 1;
    private int mCameraRequestCode = 2;

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
        mWatermarkTxt = a.getString(R.styleable.ZRUploadImageView_zr_upimageviewWatermark);
        mAlbumRequestCode = a.getInt(R.styleable.ZRUploadImageView_zr_toAlbumRequestCode, mAlbumRequestCode);
        mCameraRequestCode = a.getInt(R.styleable.ZRUploadImageView_zr_toCameraRequestCode, mCameraRequestCode);
        setTitle(title);
        setButtonText(mBtnText);
        setPlaceholder(mPlaceholder);
        mFileName = String.valueOf(hashCode()) + ".jpg";
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
        if (view == vBtn) {
            if (dialog == null) {
                dialog = new GetPicturesModeDialog(view.getContext());
                dialog.setListener(this);
            }
            dialog.show();
        }
    }

    @Override
    public int getToAlbumRequestCode() {
        return mAlbumRequestCode;
    }

    @Override
    public int getToCameraRequestCode() {
        return mCameraRequestCode;
    }

    @NotNull
    @Override
    public String getCameraSavePath() {
        return getContext().getExternalCacheDir().getAbsolutePath() + "/" + mFileName;
    }

    @Override
    public void onPicturePath(@NotNull String path) {
        Glide.with(vImg).load(path).placeholder(mPlaceholder).error(mPlaceholder).centerCrop().into(vImg);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (dialog != null) dialog.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void goCamera(@Nullable Integer toCameraRequestCode,@Nullable Uri uri) {
        if (mListener != null) mListener.goCamera(toCameraRequestCode,uri);
    }

    @Override
    public void goAlbum(@Nullable Integer toAlbumRequestCode) {
        if (mListener != null) mListener.goAlbum(toAlbumRequestCode);
    }

    public void setOnUploadImageListener(OnUploadImageListener l) {
        mListener = l;
    }


    public interface OnUploadImageListener {
        void goCamera(int requestCode, Uri uri);

        void goAlbum(int requestCode);
    }
}
