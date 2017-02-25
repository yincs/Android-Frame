package changs.android.frame.components;


import changs.android.frame.entity.action.Action;

/**
 * Created by yincs on 2017/2/7.
 */

public class Component1<SUCCESS extends Action> extends Component0 {

    protected SUCCESS success;

    public Component1(FView view, SUCCESS success) {
        super(view);
        this.success = success;
    }
}
