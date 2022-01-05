package minh.project.multishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import minh.project.multishop.R;

public class HomeViewPagerAdapter extends PagerAdapter {
    private final Integer[] urls;
    private final Context context;

    public HomeViewPagerAdapter(Integer[] urls, Context context) {
        this.urls = urls.clone();
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position %= urls.length;
        if (position < 0) {
            position = urls.length + position;
        }
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_view_page, container, false);
        ImageView imageView = itemView.findViewById(R.id.item_image);
        imageView.setImageResource(urls[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
