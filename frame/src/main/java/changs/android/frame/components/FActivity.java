package changs.android.frame.components;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import changs.android.frame.R;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yincs on 2017/2/25.
 */

public abstract class FActivity extends RxAppCompatActivity implements FGeneralView {

    private TextView tvTitle;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeLayout();
        setRootView();
        afterLayout();
        afterView(savedInstanceState);
    }

    @Override
    public void beforeLayout() {

    }

    public void setRootView() {
        final int layoutRes = getLayoutRes();
        if (layoutRes != 0)
            setContentView(layoutRes);
    }

    @Override
    public void afterLayout() {
        final View title = findViewById(R.id.f_title);
        if (title != null && title instanceof TextView)
            tvTitle = (TextView) title;

        final View back = findViewById(R.id.f_arrow_back);

        if (back != null)
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
    }

    @Override
    public FActivity getContext() {
        return this;
    }

    @Override
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    public <T> ObservableTransformer<T, T> bindToLifecycleHandle() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(FActivity.this.<T>bindToLifecycle());
            }
        };
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilDestroy() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public <T> ObservableTransformer<T, T> bindUntilDestroyHandle() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(FActivity.this.<T>bindUntilDestroy());
            }
        };
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (null != tvTitle) {
            tvTitle.setText(title);
        }
    }
}
