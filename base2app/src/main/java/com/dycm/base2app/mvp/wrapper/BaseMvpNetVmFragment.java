package com.dycm.base2app.mvp.wrapper;

import android.os.Bundle;
import androidx.databinding.ViewDataBinding;
import com.dycm.base2app.mvp.BasePresenter;
import com.dycm.base2app.mvp.wrapper.BaseNetVmFragment;
import com.dycm.base2app.mvp.wrapper.BaseViewWrapper;
import com.dycm.base2app.util.StatusBarUtil;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
public abstract class BaseMvpNetVmFragment <P extends BasePresenter, VW extends BaseViewWrapper, D extends ViewDataBinding>
        extends BaseNetVmFragment<D> {

    protected P presenter;
    protected VW viewWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        viewWrapper = createWrapper();
        if (presenter != null && viewWrapper != null) {
            presenter.setViewWrapper(viewWrapper);
        }
    }

    protected abstract P createPresenter();

    protected abstract VW createWrapper();

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
        }
        if (viewWrapper != null) {
            viewWrapper.detachView();
        }
        if (dataBinding != null) {
            dataBinding.unbind();
            dataBinding = null;
        }
        super.onDestroyView();
    }
}
