package changs.android.frame.components;

import android.app.Activity;

import io.reactivex.ObservableTransformer;

/**
 * Created by yincs on 2017/2/25.
 */

public class FSimpleView implements FView {
    protected final FView root;

    public FSimpleView(FView root) {
        this.root = root;
    }

    @Override
    public final FActivity getContext() {
        return root.getContext();
    }

    @Override
    public final void startActivity(Class<? extends Activity> cls) {
        root.startActivity(cls);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindToLifecycle() {
        return root.bindToLifecycle();
    }

    @Override
    public <T> ObservableTransformer<T, T> bindToLifecycleHandle() {
        return root.bindToLifecycleHandle();
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilDestroy() {
        return root.bindUntilDestroy();
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilDestroyHandle() {
        return root.bindUntilDestroyHandle();
    }

}
