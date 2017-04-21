package com.example.bartekpc.gl_shoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.List;

public class ProductCreationListsAdapter extends RecyclerView.Adapter
{
    private final List<Product> productList;
    private final Context context;

    public ProductCreationListsAdapter(final List<Product> list, final Context context)
    {
        this.productList = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_creation_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        final Product selectedProduct = productList.get(holder.getAdapterPosition());
        ((ViewHolder) holder).bindView(productList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return productList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView textView_productName;
        final TextView textView_productDescription;
        final TextView textView_price;

        ViewHolder(final View itemView)
        {
            super(itemView);
            textView_productName = (TextView) itemView.findViewById(R.id.text);
            textView_productDescription = (TextView) itemView.findViewById(R.id.opis);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
        }

        void bindView(Product product)
        {
            textView_productName.setText(product.getName());
            textView_productDescription.setText(String.valueOf(product.getId()));
            float totalCost = product.getPrice() * product.getAmount();
            StringBuilder totalCostBuilder = new StringBuilder();
            String totalCostText = String.valueOf(totalCostBuilder
                    .append(String.valueOf(totalCost))
                    .append(" $"));
            textView_price.setText(totalCostText);
        }
    }
}
