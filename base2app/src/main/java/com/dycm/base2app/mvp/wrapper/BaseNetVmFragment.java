package com.dycm.base2app.mvp.wrapper;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.dycm.base2app.mvp.IBaseView;
import com.dycm.base2app.mvp.rx.RxSchedulers;
import com.dycm.base2app.ui.activity.AbsActivity;
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
public abstract class BaseNetVmFragment<D extends ViewDataBinding> extends AbsBackFinishNetFragment implements IBaseView {
    public AbsActivity activity;
    private boolean isDestroyed = false;
    protected D dataBinding;
    private CompositeSubscription mCompositeSubscription;

    protected <D extends ViewDataBinding> D generateDataBinding(LayoutInflater inflater, ViewGroup container,
                                                                @LayoutRes int layoutResID) {
        D dataBinding;
        dataBinding = DataBindingUtil.inflate(inflater, layoutResID, container, false);
        return dataBinding;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        activity = (AbsActivity) getActivity();
        if (getView() != null) {
            setVisibleHint(isVisibleToUser);
        }
    }

    protected void setVisibleHint(boolean isVisibleToUser) {

    }

    @Override
    public void onDestroyView() {
        onUnSubscribe();
        activity = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }

    public void onUnSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public Subscription addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        Subscription subscription = observable.compose(RxSchedulers.<String> applySchedulers()).subscribe(subscriber);
        mCompositeSubscription.add(subscription);
        return subscription;
    }

    public Subscription addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
        return subscription;
    }
}
