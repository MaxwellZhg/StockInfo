package com.zhuorui.securities.base2app.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishNetFragment;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/12
 * Desc:
 */
public abstract class BaseMvpFinishNetFragment <P extends BasePresenter> extends AbsBackFinishNetFragment implements IBaseView {

    protected P presenter;


    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建present
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }

    }

    /**
     * 创建Presenter
     */
    protected abstract P createPresenter();
}