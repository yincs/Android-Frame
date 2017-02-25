package changs.android.frame.components;

import android.app.Activity;

import io.reactivex.ObservableTransformer;

/**
 * Created by yincs on 2017/2/25.
 */

public interface FView {
    FActivity getContext();


    void startActivity(Class<? extends Activity> cls);

    /**
     * 绑定view的生命周期，在上个事件的对应事件上取消订阅：比如onCreate~onDestroy\onResume~onPause
     *  com.trello.rxlifecycle2.android.RxLifecycleAndroid#ACTIVITY_LIFECYCLE
     *  com.trello.rxlifecycle2.android.RxLifecycleAndroid#FRAGMENT_LIFECYCLE
     */
    <T> ObservableTransformer<T, T> bindToLifecycle();

    /**
     * {@link #bindUntilDestroyHandle()}
     */
    <T> ObservableTransformer<T, T> bindToLifecycleHandle();

    /**
     * 绑定view的生命周期,在界面destroy取消订阅
     */
    <T> ObservableTransformer<T, T> bindUntilDestroy();

    /**
     * 绑定view的生命周期,在界面destroy取消订阅，并切换线程
     */
    <T> ObservableTransformer<T, T> bindUntilDestroyHandle();

}
