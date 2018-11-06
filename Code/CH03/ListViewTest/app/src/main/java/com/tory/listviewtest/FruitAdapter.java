package com.tory.listviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int mResourceID;

    public FruitAdapter(Context context, int textViewResourceID, List<Fruit> objects) {
        super(context,textViewResourceID, objects);
        mResourceID = textViewResourceID;
    }


    //Optimization
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.mFruitImage = view.findViewById(R.id.fruit_image);
            viewHolder.mFruitName = view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mFruitImage.setImageResource(fruit.getImageID());
        viewHolder.mFruitName.setText(fruit.getName());

        return view;
    }

    class ViewHolder {
        ImageView mFruitImage;
        TextView mFruitName;
    }

}
