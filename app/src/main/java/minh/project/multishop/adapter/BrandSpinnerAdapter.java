package minh.project.multishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import minh.project.multishop.R;
import minh.project.multishop.models.Brand;

import java.util.List;

public class BrandSpinnerAdapter extends ArrayAdapter<Brand> {

    private final Context mContext;
    private final List<Brand> brandList;

    public BrandSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Brand> objects) {
        super(context, resource, objects);
        mContext = context;
        brandList = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if(position == 0){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_brand,parent,false);
            ((TextView) view.findViewById(R.id.tvBrand)).setText(brandList.get(0).getBrandName());
            view.findViewById(R.id.arrow_down).setRotation(180);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_brand_drop,parent,false);
            setBrandItem(view,getItem(position));
        }
        return view;
    }

    @Nullable
    @Override
    public Brand getItem(int position) {
        if(null == brandList) return null;
        return brandList.get(position);
    }

    @Override
    public int getCount() {
        return null == brandList ? 0 : brandList.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_brand,parent,false);
        } else view = convertView;

        setBrandItem(view,getItem(position));

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setBrandItem(View view, Brand item) {
        TextView brandName = view.findViewById(R.id.tvBrand);

        if(null == item) {
            brandName.setText("Undefined");
            return;
        }
        brandName.setText(item.getBrandName());
    }
}
