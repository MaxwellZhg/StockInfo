package com.zhuorui.securities.base2app.mvp;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
public interface Presenter<V, VW> {
    void attachView(V view);

    void setViewWrapper(VW viewWrapper);

    void detachView();
}
