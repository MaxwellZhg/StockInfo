package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-18 17:45
 * desc   :
 */
public class BuyingSellingFilesView extends FrameLayout {
    public BuyingSellingFilesView(Context context) {
        this(context,null);
    }

    public BuyingSellingFilesView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BuyingSellingFilesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_buying_selling_files,this);
    }
}
