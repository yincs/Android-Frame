package changs.android.frame.entity.action;

/**
 * Created by yincs on 2017/2/7.
 */

public interface Action2<T1, T2> extends Action {
    void call(T1 t1, T2 t2);
}
