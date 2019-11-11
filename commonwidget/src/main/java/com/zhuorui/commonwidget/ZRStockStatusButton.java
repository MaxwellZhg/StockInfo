package com.zhuorui.commonwidget;

import android.content.Context;
import android.util.AttributeSet;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.commonwidget.model.Observer;
import com.zhuorui.commonwidget.model.Subject;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/29
 * Desc:
 */
public class ZRStockStatusButton extends StateButton implements Observer {
    // 0无涨跌 1涨 2跌
    private int diffState = 0;
    private LocalSettingsConfig settingsConfig;

    public ZRStockStatusButton(Context context) {
        this(context, null);
    }

    public ZRStockStatusButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZRStockStatusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        settingsConfig = LocalSettingsConfig.Companion.getInstance();
        // 监听修改配置颜色
        settingsConfig.registerObserver(this);

    }

    public void setUpDown(int diffState) {
        this.diffState = diffState;
        setUnableBackgroundColor();
    }

    private void setUnableBackgroundColor() {
        switch (diffState) {
            case 0:
                super.setUnableBackgroundColor(settingsConfig.getDefaultColor());
                break;
            case 1:
                super.setUnableBackgroundColor(settingsConfig.getUpBtnColor());
                break;
            case 2:
                super.setUnableBackgroundColor(settingsConfig.getDownBtnColor());
                break;
        }
    }

    @Override
    public void update(Subject subject) {
        settingsConfig = (LocalSettingsConfig) subject;
        // 更改字button 颜色
        setUnableBackgroundColor();
    }

}
