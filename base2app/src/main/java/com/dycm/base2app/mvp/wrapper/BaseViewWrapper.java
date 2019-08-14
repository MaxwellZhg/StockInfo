package com.dycm.base2app.mvp.wrapper;

import androidx.databinding.ViewDataBinding;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
public class BaseViewWrapper<V, D extends ViewDataBinding> implements ViewWrapper<V, D> {
    protected V view;
    protected D dataBinding;

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
        if (dataBinding != null) {
            dataBinding.unbind();
            dataBinding = null;
        }
    }

    @Override
    public void setBinding(D dataBinding) {
        this.dataBinding = dataBinding;
        onBind();
    }

    @Override
    public void onBind() {

    }

}
