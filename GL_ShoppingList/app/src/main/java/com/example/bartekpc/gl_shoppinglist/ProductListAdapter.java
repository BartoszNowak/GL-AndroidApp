package com.example.bartekpc.gl_shoppinglist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BartekPC on 3/29/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter{
    private final List<String> list;

    public ProductListAdapter(final List<String> list)
    {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        ((ViewHolder) holder).bindView(list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textView;
        ViewHolder(final View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        void bindView(String text){
            textView.setText(text);
        }
    }
}

