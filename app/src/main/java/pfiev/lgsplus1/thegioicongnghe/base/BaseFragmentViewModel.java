package pfiev.lgsplus1.thegioicongnghe.base;

import android.view.View;

public abstract class BaseFragmentViewModel<T extends BaseFragment> {
    // Fragment object
    protected T mFragment;

    /**
     * constructor
     *
     * @param t Fragment object
     */
    public BaseFragmentViewModel(T t) {
        this.mFragment = t;
    }

    /**
     * Used to initialize the layout
     *
     * @param view View object
     */
    public abstract void initView(View view);

    /**
     * Set the click event.
     *
     * @param viewId Control ID
     */
    public abstract void onClickEvent(int viewId);
}
