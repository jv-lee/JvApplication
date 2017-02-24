package com.jv.daily.net;

import android.content.Context;
import android.widget.Toast;

import com.jv.daily.widget.ProgressDialogHandler;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/2/24.
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context mContext;

    public ProgressSubscriber(Context context, SubscriberOnNextListener subscriberOnNextListener) {
        this.mSubscriberOnNextListener = subscriberOnNextListener;
        this.mContext = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.HIDE_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        Toast.makeText(mContext, "Get NewsApi Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        //在这里取消订阅
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    /**
     * 处理数据 回调接口
     *
     * @param <T>
     */
    public interface SubscriberOnNextListener<T> {
        void onNext(T t);
    }

}
