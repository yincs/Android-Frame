package changs.android.frame.components;


import changs.android.frame.entity.action.Action;

/**
 * Created by yincs on 2017/2/7.
 */

public class Component2<SUCCESS extends Action, FAIL extends Action> extends Component1<SUCCESS> {

    protected FAIL fail;

    public Component2(FView view, SUCCESS success, FAIL fail) {
        super(view, success);
        this.fail = fail;
    }
}
