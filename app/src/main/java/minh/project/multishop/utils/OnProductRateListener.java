package minh.project.multishop.utils;

import android.view.View;

public interface OnProductRateListener {

    /**
     * This function is used to make action when item is chosen.
     *
     * @param productID is the ID of the product
     * @param comment is the comment of Rating
     * @param rateIndex is the star index of Rating
     */
    void onRateProduct(int productID, String comment, int rateIndex, View rateResult, View rateComment, View rateBar);
}