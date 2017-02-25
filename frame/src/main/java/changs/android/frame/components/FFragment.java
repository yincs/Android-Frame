package changs.android.frame.components;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yincs on 2017/2/25.
 */

public abstract class FFragment<A extends FActivity> extends RxFragment implements FGeneralView {

    private A ctx;
    private View rootView;
    private boolean onCreateViewed;
    private boolean userVisibleFirst = true;

    @SuppressWarnings("unchecked")
    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = (A) context;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeLayout();
        rootView = setRootView(inflater, container);
        afterLayout();
        afterView(savedInstanceState);

        onCreateViewed = true;
        checkUserVisible();
        return rootView;
    }

    public View setRootView(LayoutInflater inflater, ViewGroup container) {
        final int layoutRes = getLayoutRes();
        if (layoutRes != 0)
            return inflater.inflate(getLayoutRes(), container, false);
        return null;
    }

    public View findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    @Override
    public FActivity getContext() {
        return ctx;
    }

    @Override
    public void beforeLayout() {

    }

    @Override
    public void afterLayout() {

    }

    @Override
    public abstract int getLayoutRes();

    @Override
    public abstract void afterView(Bundle savedInstanceState);

    @Override
    public void startActivity(Class<? extends Activity> cls) {
        ctx.startActivity(cls);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindToLifecycleHandle() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(FFragment.this.<T>bindToLifecycle());
            }
        };
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilDestroy() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilDestroyHandle() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(FFragment.this.<T>bindUntilDestroy());
            }
        };
    }


    private void checkUserVisible() {
        if (onCreateViewed && getUserVisibleHint()) {
            onUserVisible(userVisibleFirst);
            if (userVisibleFirst)
                userVisibleFirst = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            checkUserVisible();
        }
    }

    /**
     * 用户展现的时候是不是第一次
     *
     * @param isFirst true第一次展现
     */
    public void onUserVisible(boolean isFirst) {
        if (isFirst) {
            onFirstVisible();
        }
    }

    public void onFirstVisible() {
    }

}
