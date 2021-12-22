package minh.project.multishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import minh.project.multishop.R;
import minh.project.multishop.models.Image;


public class ProductViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ProductViewPagerAdapter";
    private final Image[] listImg;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
//    private final Handler mHandler;

    public ProductViewPagerAdapter(Image[] listImg, Context mContext) {
        this.listImg = listImg;
        this.mContext = mContext;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mHandler = new Handler();
    }

    @Override
    public int getCount() {
        return listImg == null ? 0 : listImg.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View viewItem = mLayoutInflater.inflate(R.layout.item_image_product,container,false);
        initView(viewItem,position);
        container.addView(viewItem);
        return viewItem;
    }

    private void initView(View view, int position) {
        ImageView imageView = view.findViewById(R.id.iv_product);
        if (position < listImg.length) { // image
//            Log.d(TAG, "initView: "+listImg[position].getUrl());
            Glide.with(mContext)
                    .load(listImg[position].getUrl())
                    .placeholder(R.drawable.progress_bar_loading)
                    .into(imageView);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
