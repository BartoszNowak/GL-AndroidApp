package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bartekpc.gl_shoppinglist.DecimalFormatUtils;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ProductCreationListsAdapter extends RecyclerView.Adapter
{
    private static final int LIST_TYPE_PREDEFINED = 0;
    private static final String EMPTY = "";

    private final List<Product> productList = new ArrayList<>();
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private final int type;
    private static String costTextResource;

    interface OnRecyclerItemClickListener
    {

        void onRecyclerItemClick(int dialogType, Product product);
    }

    ProductCreationListsAdapter(final List<Product> list, final int type, final Context context, final OnRecyclerItemClickListener onRecyclerItemClickListener)
    {
        swapList(list);
        this.type = type;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        costTextResource = context.getResources().getString(R.string.string_plus_currency);
    }

    private void swapList(final List<Product> list)
    {
        productList.clear();
        productList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_creation_list_item, parent, false);
        return new ViewHolder(view, onRecyclerItemClickListener, type, productList);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        ((ViewHolder) holder).bindView(productList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return productList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView textView_productName;
        final TextView textView_price;

        private OnRecyclerItemClickListener onRecyclerItemClickListener;
        private int type;
        private List<Product> productList;

        ViewHolder(final View itemView, final OnRecyclerItemClickListener onRecyclerItemClickListener, final int type, final List<Product> productList)
        {
            super(itemView);
            textView_productName = (TextView) itemView.findViewById(R.id.text);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            this.onRecyclerItemClickListener = onRecyclerItemClickListener;
            this.type = type;
            this.productList = productList;
            itemView.setOnClickListener(this);
        }

        void bindView(Product product)
        {
            textView_productName.setText(product.getName());
            if(type == LIST_TYPE_PREDEFINED)
            {
                textView_price.setText(EMPTY);
            }
            else
            {
                float totalCost = product.getPrice() * product.getAmount();
                String totalCostText = String.format(Locale.getDefault(), costTextResource, DecimalFormatUtils.formatCurrency(totalCost));
                textView_price.setText(totalCostText);
            }
        }

        @Override
        public void onClick(final View view)
        {
            onRecyclerItemClickListener.onRecyclerItemClick(type, productList.get(getAdapterPosition()));
        }
    }
}
