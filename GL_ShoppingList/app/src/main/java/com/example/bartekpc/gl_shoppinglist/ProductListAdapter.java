package com.example.bartekpc.gl_shoppinglist;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by BartekPC on 3/29/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter
{
    private final List<Product> productList;
    private final Context context;

    public ProductListAdapter(final List<Product> list, final Context context)
    {
        this.productList = list;
        this.context = context;
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
        ((ViewHolder) holder).bindView(productList.get(position));
        ((ViewHolder) holder).checkBox_purchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
            {
                ((ProductListActivity)context).setProductPurchased(holder.getAdapterPosition(), isChecked);
            }
        });
        ((ProductListAdapter.ViewHolder) holder).buttonViewOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                final PopupMenu popup = new PopupMenu(context, ((ProductListAdapter.ViewHolder) holder).buttonViewOption);
                popup.inflate(R.menu.product_options_menu);
                popup.getMenu().getItem(2).setChecked(productList.get(holder.getAdapterPosition()).isFavourite());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                ((ProductListActivity)context).buildUpdateProductDialog(holder.getAdapterPosition());
                                break;
                            case R.id.menu2:
                                ((ProductListActivity)context).deleteProduct(holder.getAdapterPosition());
                                break;
                            case R.id.menu3:
                                ((ProductListActivity)context).setProductFavourite(holder.getAdapterPosition(), !productList.get(holder.getAdapterPosition()).isFavourite());
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
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
        final TextView textView_priceDetails;
        final TextView buttonViewOption;
        final CheckBox checkBox_purchase;

        ViewHolder(final View itemView)
        {
            super(itemView);
            textView_productName = (TextView) itemView.findViewById(R.id.text);
            textView_productDescription = (TextView) itemView.findViewById(R.id.opis);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            textView_priceDetails = (TextView) itemView.findViewById(R.id.textView_PriceDetails);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textView_options);
            checkBox_purchase = (CheckBox) itemView.findViewById(R.id.checkbox_purchase);
        }

        void bindView(Product product)
        {
            textView_productName.setText(product.getName());
            textView_productDescription.setText(String.valueOf(product.getCatalogId()));
            float totalCost = product.getPrice() * product.getAmount();
            StringBuilder totalCostBuilder = new StringBuilder();
            String totalCostText = String.valueOf(totalCostBuilder
                    .append(String.valueOf(totalCost))
                    .append(" $"));
            textView_price.setText(totalCostText);
            StringBuilder costBuilder = new StringBuilder();
            String costDetails = String.valueOf(costBuilder
                    .append("(")
                    .append(String.valueOf(product.getAmount()))
                    .append(" x ")
                    .append(String.valueOf(product.getPrice()))
                    .append(" $")
                    .append(")"));
            textView_priceDetails.setText(costDetails);
            checkBox_purchase.setChecked(product.isPurchased());
        }
    }
}
