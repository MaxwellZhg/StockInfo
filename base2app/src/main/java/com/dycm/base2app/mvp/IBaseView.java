package com.dycm.base2app.mvp;

import android.content.Context;

/**
 * Describe：所有View基类
 * Created by maxwell.
 */

public interface IBaseView {
    /**
     * 上下文
     *
     * @return context
     */
    Context getContext();

    public boolean isDestroyed();
}
