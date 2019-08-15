package com.zhuorui.securities.base2app.mvp.wrapper;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
public  interface ViewWrapper<V, D> {
    void attachView(V view);

    void detachView();

    void setBinding(D dataBinding);

    void onBind();
}
