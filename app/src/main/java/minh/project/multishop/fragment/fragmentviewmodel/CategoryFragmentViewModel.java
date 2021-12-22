package minh.project.multishop.fragment.fragmentviewmodel;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import minh.project.multishop.adapter.CategoryAdapter;
import minh.project.multishop.adapter.ProductCateAdapter;
import minh.project.multishop.base.BaseFragmentViewModel;
import minh.project.multishop.databinding.FragmentCategoryBinding;
import minh.project.multishop.fragment.CategoryFragment;
import minh.project.multishop.models.Brand;
import minh.project.multishop.models.Category;
import minh.project.multishop.models.Product;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import minh.project.multishop.network.dtos.DTOResponse.GetListProductResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragmentViewModel extends BaseFragmentViewModel<CategoryFragment> {
    private static final String TAG = "CategoryFragment";
    private ProductCateAdapter productCateAdapter;

    //View
    private RecyclerView mCategoryView;
    private RecyclerView mProductView;
    private LinearLayout noProductView;
    private LinearLayout mSpinner;

    private int firstPosition;
    private List<Category> categoryList;
    private List<Product> productList;

    private FragmentCategoryBinding mBinding;


    private MutableLiveData<List<Category>> liveCateData;
    private MutableLiveData<List<Product>> liveProdData;
    private final IAppAPI api;

    public LiveData<List<Category>> getListCategory(){
        if(liveCateData==null){
            liveCateData = new MutableLiveData<>();
            loadCateData();
        }
        return liveCateData;
    }

    public LiveData<List<Product>> getProdByCate(int cateID){
        liveProdData = new MutableLiveData<>();
        loadProdData(cateID);
        return liveProdData;
    }

    private void loadProdData(int id) {
        Call<GetListProductResponse> call = api.getProductByCategory(id,40);
        call.enqueue(new Callback<GetListProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetListProductResponse> call, @NonNull Response<GetListProductResponse> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        liveProdData.postValue(response.body().productList);
//                        Log.d(TAG, "Response size: "+result.size());
                    } else {
                        liveProdData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetListProductResponse> call, @NonNull Throwable t) {
                liveProdData.postValue(null);
            }
        });
    }

    private void loadCateData() {
        Call<List<Category>> call = api.getAllCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()){
                    if(response.body()==null) return;
                    liveCateData.postValue(response.body());
                } else {
                    liveCateData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                liveCateData.postValue(null);
            }
        });
    }

    /**
     * constructor
     *
     * @param categoryFragment Fragment object
     */
    public CategoryFragmentViewModel(CategoryFragment categoryFragment) {
        super(categoryFragment);
        api = RetroInstance.getAppAPI();
    }

    @Override
    public void initView(View view) {
        mBinding = mFragment.getBinding();

        mCategoryView = mBinding.recyclerCatalogueType;
        LinearLayoutManager layoutManager = new LinearLayoutManager(mFragment.getContext());
        mCategoryView.setLayoutManager(layoutManager);

        mProductView = mBinding.recyclerCatalogueProduct;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mFragment.getContext(), 2);
        mProductView.setLayoutManager(gridLayoutManager);
        mProductView.setNestedScrollingEnabled(false);
        productList = new ArrayList<>();
        productCateAdapter = new ProductCateAdapter(mFragment.getContext(),productList);
        mProductView.setAdapter(productCateAdapter);

        noProductView = mBinding.lvNoProduct;
        mSpinner = mBinding.lvProgress;
        initCategoryView(firstPosition);
        initProductView(1);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initCategoryView(int showPosition){
        categoryList = new ArrayList<>();

        CategoryAdapter adapter = new CategoryAdapter(categoryList,mFragment.getContext(),showPosition);
        mCategoryView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            initProductView(categoryList.get(position).getID());
            initBrandSpinner(categoryList.get(position).getBrandList());
            mSpinner.setVisibility(View.VISIBLE);
        });

        getListCategory().observe(mFragment.getViewLifecycleOwner(), categories -> {
            if (categories==null){
                noProductView.setVisibility(View.VISIBLE);
                return;
            }
            categoryList.addAll(categories);
            adapter.notifyDataSetChanged();
        });
    }

    private void initBrandSpinner(List<Brand> brandList) {
        List<String> brands = new ArrayList<>();
        brands.add("(Tất cả)");
        for(Brand b : brandList) brands.add(b.getBrandName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mFragment.getContext(), android.R.layout.simple_spinner_item,brands);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerBrand.setAdapter(adapter);
        mBinding.spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelectedHandler(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        if(position == 0){
            productCateAdapter.setProductList(productList);
            return;
        }

        Adapter adapter = adapterView.getAdapter();
        String search = (String) adapter.getItem(position);

        List<Product> searchedProducts = queryByBrandName(search);
        productCateAdapter.setProductList(searchedProducts);
    }

    private List<Product> queryByBrandName(String search) {
        List<Product> result = new ArrayList<>();
        for(Product p : productList){
            if(search.equals(p.getBrand().getBrandName())) result.add(p);
        }
        return result;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initProductView(int category) {
        getProdByCate(category).observe(mFragment.getViewLifecycleOwner(), products -> {
            if(products==null || products.size()==0){
                mSpinner.setVisibility(View.GONE);
                noProductView.setVisibility(View.VISIBLE);
                mProductView.setVisibility(View.GONE);
                return;
            }

            productList.clear();
            productList.addAll(products);
            productCateAdapter.setProductList(productList);

            noProductView.setVisibility(View.GONE);
            mProductView.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClickEvent(int viewId) {

    }

    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }
}
