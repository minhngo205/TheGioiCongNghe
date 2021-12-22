package minh.project.multishop.fragment.fragmentviewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import minh.project.multishop.R;
import minh.project.multishop.adapter.HomeProductAdapter;
import minh.project.multishop.adapter.HomeViewPagerAdapter;
import minh.project.multishop.base.BaseFragmentViewModel;
import minh.project.multishop.databinding.FragmentHomeBinding;
import minh.project.multishop.fragment.HomeFragment;
import minh.project.multishop.models.Product;
import minh.project.multishop.network.IAppAPI;
import minh.project.multishop.network.RetroInstance;
import minh.project.multishop.network.dtos.DTOResponse.GetListProductResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentViewModel extends BaseFragmentViewModel<HomeFragment> {

    private static final String TAG = "HomeFragmentViewModel";
    private static final int VIEW_PAGER_HEIGHT = 846;
    private MutableLiveData<List<Product>> liveData;
    private FragmentHomeBinding homeBinding;
    private HomeProductAdapter adapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    private List<View> dots;
    private final HomeImageHandler handler;

    private RecyclerView rvRecommendation;
    private ViewPager viewPager;
    private LinearLayout lvDot;


    public HomeImageHandler getHandler() {
        return handler;
    }

    /**
     * constructor
     *
     * @param homeFragment Fragment object
     */
    public HomeFragmentViewModel(HomeFragment homeFragment) {
        super(homeFragment);
        handler = new HomeImageHandler(new WeakReference<>(this));
    }

    public LiveData<List<Product>> getListProduct(){
        if(liveData==null){
            liveData = new MutableLiveData<>();
            loadProductData();
        }
        return liveData;
    }

    private void loadProductData() {
        IAppAPI api = RetroInstance.getAppAPI();
        Call<GetListProductResponse> call = api.getHomeListProduct();
        call.enqueue(new Callback<GetListProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetListProductResponse> call, @NonNull Response<GetListProductResponse> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        liveData.postValue(response.body().productList);
                    } else {
                        liveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetListProductResponse> call, @NonNull Throwable t) {
                liveData.postValue(null);
            }
        });
    }

    @Override
    public void initView(View view) {
        homeBinding = mFragment.getHomeBinding();

        rvRecommendation = homeBinding.recyclerRecommendation;
        viewPager = homeBinding.pagerHome;
        lvDot = homeBinding.layoutDot;

        mShimmerViewContainer = homeBinding.shimmerViewContainer;
        GridLayoutManager layoutManager = new GridLayoutManager(mFragment.getActivity(), 2);
        rvRecommendation.setLayoutManager(layoutManager);
        rvRecommendation.setItemAnimator(new DefaultItemAnimator());
        rvRecommendation.setNestedScrollingEnabled(false);

        setViewPagerHeight();
    }

    public void initHomeProductRecyclerView() {

        getListProduct().observe(mFragment.getViewLifecycleOwner(), products -> {
            if(products==null){
                Toast.makeText(mFragment.getContext(), "Could not get product Data", Toast.LENGTH_SHORT).show();
                return;
            }
            adapter = new HomeProductAdapter(products,mFragment.getContext());
            rvRecommendation.setAdapter(adapter);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId) {
            case R.id.card_new:

            case R.id.card_article1:

            case R.id.card_article2:

            default:
                break;
        }
    }

    public void StopAnimation(){
        mShimmerViewContainer.stopShimmer();
    }

    public void StartAnimation(){
        mShimmerViewContainer.startShimmer();
    }

    public void initViewPager() {
        dots = new ArrayList<>();

        Integer[] images = new Integer[]{R.mipmap.banner, R.mipmap.banner0,R.mipmap.banner1};

        for (int i = 0; i < images.length; i++) {
            // Initializing dots
            ImageView dot =
                    (ImageView) LayoutInflater.from(mFragment.getContext()).inflate(R.layout.dot_image_view, lvDot, false);
            lvDot.addView(dot);
            dots.add(dot);
        }

        viewPager.setAdapter(new HomeViewPagerAdapter(images, mFragment.getContext()));

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        showPoint(position);
                        handler.sendMessage(Message.obtain(handler, HomeImageHandler.CHANGED, position, 0));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        switch (state) {
                            case ViewPager.SCROLL_STATE_DRAGGING:
                                handler.sendEmptyMessage(HomeImageHandler.PAUSE);
                                break;
                            case ViewPager.SCROLL_STATE_IDLE:
                                handler.sendEmptyMessageDelayed(HomeImageHandler.UPDATE, HomeImageHandler.TIME_DELAY);
                                break;
                            default:
                                break;
                        }
                    }
                });

        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        viewPager.setCurrentItem(0);

        // Start the NVOD effect.
        handler.sendEmptyMessageDelayed(HomeImageHandler.UPDATE, HomeImageHandler.TIME_DELAY);
    }

    private void setViewPagerHeight() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = VIEW_PAGER_HEIGHT;
        viewPager.setLayoutParams(layoutParams);
    }

    private void showPoint(int position) {
        if (dots == null) {
            return;
        }
        int size = dots.size();
        for (int i = 0; i < size; i++) {
            dots.get(i).setBackgroundResource(R.drawable.dot_no_selected);
        }
        dots.get(position % size).setBackgroundResource(R.drawable.dot_selected);
    }

    public void setCurrentPosition(int currentPosition) {
        setViewPageScrollTime(viewPager);
        viewPager.setCurrentItem(currentPosition, true);
    }

    private void setViewPageScrollTime(ViewPager mViewPager){
        Field field;
        try {
            field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(mViewPager, scroller);
            scroller.setmDuration(1000);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class HomeImageHandler extends Handler {
        /**
         * Time interval
         */
        public static final long TIME_DELAY = 3000;

        /**
         * Request to update the displayed viewPager
         */
        public static final int UPDATE = 1;

        /**
         * Request to pause
         */
        public static final int PAUSE = 2;

        /**
         * Request to restart
         */
        public static final int RESTART = 3;

        /**
         * Record the latest page number.
         */
        public static final int CHANGED = 4;

        /**
         * Weak reference is used to prevent handler leakage.
         */
        private final WeakReference<HomeFragmentViewModel> wk;

        private int currentItem = Integer.MAX_VALUE / 2;

        public HomeImageHandler(WeakReference<HomeFragmentViewModel> wk) {
            this.wk = wk;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HomeFragmentViewModel viewModel = wk.get();
            if (viewModel == null) {
                return;
            }

            // Check the message queue and remove unsent messages to avoid duplicate messages in complex environments.
            // This part will eat the first auto-rotation event, so you can add a condition, The event is cleared only
            // when the value is Position!=Max/2.
            // Because the first position must be equal to Max/2.
            if ((viewModel.getHandler().hasMessages(UPDATE)) && (currentItem != Integer.MAX_VALUE / 2)) {
                viewModel.getHandler().removeMessages(UPDATE);
            }
            switch (msg.what) {
                case UPDATE:
                    currentItem++;
                    viewModel.setCurrentPosition(currentItem);
                    viewModel.getHandler().sendEmptyMessageDelayed(UPDATE, TIME_DELAY);
//                    Log.d(TAG, "UPDATE");
                    break;
                case PAUSE:
//                    Log.d(TAG, "PAUSE");
                    break;
                case RESTART:
                    viewModel.getHandler().sendEmptyMessageDelayed(UPDATE, TIME_DELAY);
//                    Log.d(TAG, "RESTART");
                    break;
                case CHANGED:
                    currentItem = msg.arg1;
//                    Log.d(TAG, "CHANGED");
                    break;
                default:
                    break;
            }
        }
    }

    public static class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        /**
         * setmDuration
         * @param time ms
         */
        public void setmDuration(int time) {
            mDuration = time;
        }

    }
}