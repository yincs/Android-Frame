package changs.android.frame.entity.action;

/**
 * Created by yincs on 2017/2/7.
 */

public interface Action1<T> extends Action {
    void call(T t);
}
