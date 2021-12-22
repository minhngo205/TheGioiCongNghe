package minh.project.multishop.utils;

import android.view.View;

public interface OnItemModifyListener {
    /**
     * This function is used to make action when item is chosen.
     *
     * @param position item position
     * @param isChecked selected or not
     */
    void onItemChoose(int position, boolean isChecked);

    /**
     * This function is used to make action when quantity is added.
     *
     * @param position item position
     * @param quantityView view for displaying quantity
     */
    void onItemQuantityAdd(int position, View quantityView);

    /**
     * This function is used to make action when quantity is reduced.
     *
     * @param position item position
     * @param quantityView view for displaying quantity
     */
    void onItemQuantityReduce(int position, View quantityView);
}
