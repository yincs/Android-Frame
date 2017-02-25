package changs.android.frame.components;

import changs.android.frame.entity.action.Action1;

/**
 * Created by yincs on 2017/2/26.
 */

public class Component extends Component2<Action1<Object>, Action1<Object>> {
    public Component(FView view, Action1<Object> success, Action1<Object> fail) {
        super(view, success, fail);
    }
}
