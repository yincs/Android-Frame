package changs.android.frame.components;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

/**
 * Created by yincs on 2017/2/25.
 */

interface FGeneralView extends FView {
    void beforeLayout();

    void afterLayout();

    @LayoutRes
    int getLayoutRes();

    void afterView(Bundle savedInstanceState);
}
