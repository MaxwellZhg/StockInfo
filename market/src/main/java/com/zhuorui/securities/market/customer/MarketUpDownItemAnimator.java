package com.zhuorui.securities.market.customer;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-28 13:58
 * desc   : 涨跌Item子顶渐变动画 (PS:item 的背景不能有透明度，否则更新时会有闪烁)
 */
public class MarketUpDownItemAnimator extends BaseItemAnimator {


    /**
     * 执行移除动画
     *
     * @param holder   被移除的ViewHolder
     * @param animator 被移动的ViewHolder对应动画对象
     */
    @Override
    public void setRemoveAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.alpha(0);
    }

    /**
     * 执行移除动画结束，执行还原，因为该ViewHolder会被复用
     *
     * @param view 被移除的ViewHolder
     */
    @Override
    public void removeAnimationEnd(RecyclerView.ViewHolder view) {
        ViewCompat.setAlpha(view.itemView, 1);
    }

    /**
     * 执行添加动画初始化 这里设置透明为0添加时就会有渐变效果当然你可以在执行动画代码之前执行
     *
     * @param holder 添加的ViewHolder
     */
    @Override
    public void addAnimationInit(RecyclerView.ViewHolder holder) {
        ViewCompat.setAlpha(holder.itemView, 0);
    }

    /**
     * 执行添加动画
     *
     * @param holder   添加的ViewHolder
     * @param animator 添加的ViewHolder对应动画对象
     */
    @Override
    public void setAddAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.alpha(1);
    }

    /**
     * 取消添加还原状态以复用
     *
     * @param holder 添加的ViewHolder
     */
    @Override
    public void addAnimationCancel(RecyclerView.ViewHolder holder) {
        ViewCompat.setAlpha(holder.itemView, 1);
    }

    /**
     * 更新时旧的ViewHolder动画
     *
     * @param holder   旧的ViewHolder
     * @param animator ViewHolder对应动画对象
     */
    @Override
    public void setOldChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        //设置成动画结束状态，使item不显示动画效果，防止干扰子项效果显示
        animator.translationX(0);
        animator.translationY(0);
        animator.alpha(1);
    }

    /**
     * 更新时旧的ViewHolder动画结束，执行还原
     *
     * @param holder
     */
    @Override
    public void oldChangeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setAlpha(holder.itemView, 1);
    }

    /**
     * 更新时新的ViewHolder初始化
     *
     * @param holder 更新时新的ViewHolder
     */
    @Override
    public void newChangeAnimationInit(RecyclerView.ViewHolder holder) {
        //初始化设置动画结束状态，使item不显示动画效果，防止干扰子项效果显示
        ViewCompat.setAlpha(holder.itemView, 1);
        ViewCompat.setTranslationX(holder.itemView, 0);
        ViewCompat.setTranslationY(holder.itemView, 0);
    }

    /**
     * 更新时新的ViewHolder动画
     *
     * @param holder   新的ViewHolder
     * @param animator ViewHolder对应动画对象
     */
    @Override
    public void setNewChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        animator.translationX(0);
        animator.translationY(0);
        animator.alpha(1);
    }

    /**
     * 更新时新的ViewHolder动画结束，执行还原
     *
     * @param holder
     */
    @Override
    public void newChangeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setAlpha(holder.itemView, 1);
    }

    /**
     * 更新时旧的ViewHolder子项动画
     */
    @Override
    protected ViewPropertyAnimatorCompat getOldChildViewChangeAnim(IItemChildHolder holder) {
        View vAnim = holder.getChildChangeAnimView();
        vAnim.setAlpha(0);
        ViewPropertyAnimatorCompat animator = ViewCompat.animate(vAnim);
        animator.alpha(1);
        animator.setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                vAnim.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {
                vAnim.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {
                vAnim.setVisibility(View.GONE);
            }
        });
        return animator;
    }

    /**
     * 更新时新的ViewHolder子项动画
     */
    @Override
    protected ViewPropertyAnimatorCompat getNewChildViewChangeAnim(IItemChildHolder holder) {
        View vAnim = holder.getChildChangeAnimView();
        vAnim.setAlpha(1);
        ViewPropertyAnimatorCompat animator = ViewCompat.animate(vAnim);
        animator.alpha(0);
        animator.setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                vAnim.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(View view) {
                vAnim.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {
                vAnim.setVisibility(View.GONE);
            }
        });
        return animator;
    }
}
