package minh.project.multishop.activity.viewmodel;

import static minh.project.multishop.base.BaseDialog.CANCEL_BUTTON;
import static minh.project.multishop.base.BaseDialog.CONFIRM_BUTTON;
import static minh.project.multishop.base.BaseDialog.CONTENT;
import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minh.project.multishop.R;
import minh.project.multishop.activity.CartActivity;
import minh.project.multishop.activity.OrderSubmitActivity;
import minh.project.multishop.adapter.CartAdapter;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.base.BaseDialog;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityCartBinding;
import minh.project.multishop.models.CartItem;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.network.dtos.DTORequest.EditCartRequest;
import minh.project.multishop.network.repository.CartRepository;
import minh.project.multishop.utils.CustomProgress;
import minh.project.multishop.utils.SwipeHelper;

public class CartActivityViewModel extends BaseActivityViewModel<CartActivity> {

    private ActivityCartBinding mBinding;
    private final UserDBRepository dbRepository;
    private final CartRepository cartRepository;
    private final CartAdapter cartAdapter;
    private final User mUser;
    private List<CartItem> cartList;

    private TextView textActualPrice;
    private CheckBox checkAllSelect;
    private RecyclerView recyclerBagList;
    private LinearLayout layoutLoginFirst;
    private RelativeLayout layoutBottom;
    private int totalPrice;
    private int totalQuantity;
    private Map<Integer,Boolean> checkedMap;

//    private Map<Integer,Boolean> checkState;

    /**
     * constructor
     *
     * @param cartActivity Activity object
     */
    public CartActivityViewModel(CartActivity cartActivity) {
        super(cartActivity);
        cartList = new ArrayList<>();
        checkedMap = new HashMap<>();
        dbRepository = UserDBRepository.getInstance();
        cartRepository = CartRepository.getInstance();
        mUser = dbRepository.getCurrentUser();
        cartAdapter = new CartAdapter(mActivity);
    }

    @Override
    public void initView() {
        mBinding = mActivity.getBinding();
        cartAdapter.setOnItemModifyListener(mActivity);

        mBinding.titleOrderEvaluation.tvTitle.setText(mActivity.getString(R.string.cart_title));

        textActualPrice = mBinding.textViewActualPrice;
        textActualPrice.setText(currencyFormat(0));

//        textShipCost = mBinding.textViewShipCost;
        mBinding.textViewPay.setOnClickListener(mActivity);
        checkAllSelect = mBinding.checkBoxAllSelect;

        recyclerBagList = mBinding.listShoppingCart;
        recyclerBagList.setAdapter(cartAdapter);
        recyclerBagList.setLayoutManager(new LinearLayoutManager(mActivity));

        SwipeHelper swipeHelper = new SwipeHelper(mActivity, recyclerBagList, 150) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {

                buffer.add(new MyButton(mActivity,
                        R.drawable.ic_trash_can_regular__1_,
                        Color.parseColor("#D81B60"),
                        pos -> showDeleteDialog(pos)
                ));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
        itemTouchHelper.attachToRecyclerView(recyclerBagList);

        layoutLoginFirst = mBinding.layoutLoginFirst;
        layoutBottom = mBinding.rlBottom;

        mBinding.titleOrderEvaluation.ivBack.setOnClickListener(mActivity);
        mBinding.textViewPay.setOnClickListener(mActivity);
        checkAllSelect.setOnClickListener(mActivity);
    }

    private void showDeleteDialog(int pos) {
        Bundle data = new Bundle();
        data.putString(CONFIRM_BUTTON, mActivity.getString(R.string.confirm));
        data.putString(CANCEL_BUTTON, mActivity.getString(R.string.cancel));
        data.putString(CONTENT, mActivity.getString(R.string.confirm_cart_item_delete));

        BaseDialog dialog = new BaseDialog(mActivity, data, true);
        dialog.setConfirmListener(v -> {
            deleteItem(pos);
            dialog.dismiss();
        });
        dialog.setCancelListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void deleteItem(int position) {
        int deleteID = cartList.get(position).getID();
        CustomProgress dialog = CustomProgress.getInstance();
        dialog.showProgress(mActivity,"Đang xoá...",false);
        cartRepository.deleteCartItem(mUser.getAccToken(),deleteID).observe(mActivity, s -> {
            if(null == s || s.isEmpty()){
                Toast.makeText(mActivity, "Đã xảy ra lỗi. Không thể xoá giỏ hàng", Toast.LENGTH_SHORT).show();
                dialog.hideProgress();
                return;
            }

            reloadData(getCheckedMap());
            dialog.hideProgress();
            Toast.makeText(mActivity, "Server: "+s, Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.checkBox_all_select:{
                if (cartList.size() != 0) {
                    if (checkAllSelect.isChecked()) {
                        for (CartItem shoppingCart : cartList) {
                            shoppingCart.setChoose(true);
                        }
                    } else {
                        for (CartItem shoppingCart : cartList) {
                            shoppingCart.setChoose(false);
                        }
                    }
                    modifyData(cartList);
                }
                break;
            }
            case R.id.textView_pay:{
                if(0==totalQuantity){
                    Toast.makeText(mActivity, "Hãy chọn ít nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<OrderItem> orderItemData = GenerateOrder();
                    ArrayList<Integer> listCartID = getListID();
                    Intent intent = new Intent(mActivity, OrderSubmitActivity.class);
                    intent.putParcelableArrayListExtra("ORDER_DATA",orderItemData);
                    intent.putIntegerArrayListExtra("LIST_CART_ID",listCartID);
                    intent.putExtra("TOTAL_PRICE",totalPrice);
                    mActivity.startActivity(intent);
                }
                break;
            }
            case R.id.iv_back:{
                mActivity.finish();
                break;
            }
            default: break;
        }
    }

    private ArrayList<Integer> getListID() {
        ArrayList<Integer> result = new ArrayList<>();
        for(CartItem cartItem : cartList){
            if(cartItem.isChoose()) result.add(cartItem.getID());
        }
        return result;
    }

    private ArrayList<OrderItem> GenerateOrder() {
        ArrayList<OrderItem> result = new ArrayList<>();
        for(CartItem cartItem : cartList){
            if(cartItem.isChoose()) result.add(cartItem.toOrderItem());
        }
        return result;
    }

    public void initData() {
        if(null == mUser){
            cartList = new ArrayList<>();
//            textEdit.setVisibility(View.GONE);
            layoutLoginFirst.setVisibility(View.VISIBLE);
            recyclerBagList.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.GONE);
        } else {
            cartRepository.getListCartData(mUser.getAccToken()).observe(mActivity, cartItems -> {
                if(null == cartItems){
                    Toast.makeText(mActivity, "Không thể nhận thông tin từ máy chủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                cartList.addAll(cartItems);

                checkNoProduct();

                cartAdapter.setCartItems(cartList);
            });
        }
    }

    private void modifyData(List<CartItem> bagList) {
        this.cartList = bagList;
        cartAdapter.setCartItems(bagList);
        checkNoProduct();
        statistics();
    }

    public void statistics() {
        totalPrice = 0;
        totalQuantity = 0;
        if (null != cartList) {
            for (int i = 0; i < cartList.size(); i++) {
                CartItem item = cartList.get(i);
                if (item.isChoose()) {
                    totalQuantity += item.getCount();
                    totalPrice += item.getProduct().getSalePrice() * item.getCount();
                }
            }
        }
        if (0 == totalQuantity) {
            checkAllSelect.setChecked(false);
        }
        textActualPrice.setText(currencyFormat(totalPrice));
    }

    public void onItemChoose(int position, boolean isChecked) {
        cartList.get(position).setChoose(isChecked);
        checkAllSelect.setChecked(isAllChosen());
        modifyData(cartList);
    }

    public void onItemQuantityAdd(int position, View quantityView) {
        CartItem shoppingCart = cartList.get(position);
        CustomProgress dialog = CustomProgress.getInstance();
        dialog.showProgress(mActivity,"Đang thêm...",false);
        EditCartRequest request = new EditCartRequest(shoppingCart.getProduct().productID,1);
        cartRepository.getAddCartData(mUser.getAccToken(),request).observe(mActivity, editCartResponse -> {
            if(editCartResponse==null){
                Toast.makeText(mActivity, "Không thể chỉnh sửa giỏ hàng", Toast.LENGTH_SHORT).show();
                dialog.hideProgress();
                return;
            }

            reloadData(getCheckedMap());
            dialog.hideProgress();
        });
    }

    public void reloadData(Map<Integer, Boolean> checkedMap) {
        cartList.clear();
        cartRepository.getListCartData(mUser.getAccToken()).observe(mActivity, cartItems -> {
            cartList = cartItems;
            for(CartItem item : cartList){
                if(checkedMap.containsKey(item.getID())) item.setChoose(true);
            }
            modifyData(cartList);
        });
    }

    public void onItemQuantityReduce(int position, View quantityView) {
        CartItem shoppingCart = cartList.get(position);
        if (shoppingCart.getCount()==1){
            showDeleteDialog(position);
            return;
        }
        CustomProgress dialog = CustomProgress.getInstance();
        dialog.showProgress(mActivity,"Đang bớt...",false);
        EditCartRequest request = new EditCartRequest(shoppingCart.getProduct().productID,1);
        cartRepository.getRemoveCartData(mUser.getAccToken(),request).observe(mActivity, editCartResponse -> {
            if(editCartResponse==null){
                Toast.makeText(mActivity, "Không thể chỉnh sửa giỏ hàng", Toast.LENGTH_SHORT).show();
                dialog.hideProgress();
                return;
            }

            reloadData(getCheckedMap());
            dialog.hideProgress();
        });
    }

    private boolean isAllChosen() {
        if (cartList.isEmpty()) {
            return false;
        }
        for (CartItem shoppingCart : cartList) {
            if (!shoppingCart.isChoose()) {
                return false;
            }
        }
        return true;
    }

    public Map<Integer, Boolean> getCheckedMap() {
        checkedMap = new HashMap<>();
        for(CartItem item : cartList){
            if(item.isChoose()){
                checkedMap.put(item.getID(), true);
            }
        }
        return checkedMap;
    }

    private void checkNoProduct(){
        if (cartList.isEmpty()){
            mBinding.llNoItems.setVisibility(View.VISIBLE);
            return;
        }
        mBinding.llNoItems.setVisibility(View.GONE);
        layoutLoginFirst.setVisibility(View.GONE);
        recyclerBagList.setVisibility(View.VISIBLE);
        layoutBottom.setVisibility(View.VISIBLE);
    }
}
