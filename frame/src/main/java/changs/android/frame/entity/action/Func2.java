package changs.android.frame.entity.action;

/**
 * Created by yincs on 2017/2/14.
 */

public interface Func2<R, T1, T2> extends Function {
    R call(T1 t1, T2 t2);
}
