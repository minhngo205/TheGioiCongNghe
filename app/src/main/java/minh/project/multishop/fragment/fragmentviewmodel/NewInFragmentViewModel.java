package minh.project.multishop.fragment.fragmentviewmodel;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import minh.project.multishop.adapter.HomeProductAdapter;
import minh.project.multishop.base.BaseFragmentViewModel;
import minh.project.multishop.databinding.FragmentNewInBinding;
import minh.project.multishop.fragment.NewInFragment;
import minh.project.multishop.models.Product;
import minh.project.multishop.network.repository.ProductRepository;


public class NewInFragmentViewModel extends BaseFragmentViewModel<NewInFragment> {

    private FragmentNewInBinding mBinding;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView rvNewIn;
    private final ProductRepository mProductRepository;
    private HomeProductAdapter adapter;

    /**
     * constructor
     *
     * @param newInFragment Fragment object
     */
    public NewInFragmentViewModel(NewInFragment newInFragment) {
        super(newInFragment);
        mProductRepository = ProductRepository.getInstance();
    }

    @Override
    public void initView(View view) {
        mBinding = mFragment.getBinding();

        mShimmerViewContainer = mBinding.shimmerViewContainer;
        rvNewIn = mBinding.recyclerNewIn;

        GridLayoutManager layoutManager = new GridLayoutManager(mFragment.getActivity(), 2);
        rvNewIn.setLayoutManager(layoutManager);
        rvNewIn.setItemAnimator(new DefaultItemAnimator());
        rvNewIn.setNestedScrollingEnabled(false);
    }

    public void initNewInProductRV(){
        mProductRepository.getAllProduct().observe(mFragment.getViewLifecycleOwner(), products -> {
            if(products==null){
                Toast.makeText(mFragment.getContext(), "Could not get product Data", Toast.LENGTH_SHORT).show();
                return;
            }
            adapter = new HomeProductAdapter(getDiscountProduct(products),mFragment.getContext());
            rvNewIn.setAdapter(adapter);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        });
    }

    private List<Product> getDiscountProduct(List<Product> products) {
        List<Product> result = new ArrayList<>();
        for (Product product : products){
            if(product.isDiscount!=0) result.add(product);
        }
        return result;
    }

    public void StopAnimation(){
        mShimmerViewContainer.stopShimmer();
    }

    public void StartAnimation(){
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onClickEvent(int viewId) {

    }
}
