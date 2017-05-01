package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bartekpc.gl_shoppinglist.DecimalFormatUtils;
import com.example.bartekpc.gl_shoppinglist.DialogFactory;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.List;
import java.util.Locale;

public class ProductCreationListsAdapter extends RecyclerView.Adapter
{
    public interface OnRecyclerItemClickListener {

        void onRecyclerItemClick(int dialogType, Product product);
    }

    private final List<Product> productList;
    private final Context context;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private final int type;

    /*public ProductCreationListsAdapter(final List<Product> list, final Context context)
    {
        this.productList = list;
        this.context = context;

        this.type = 0;
    }*/

    public ProductCreationListsAdapter(final List<Product> list, final Context context, final OnRecyclerItemClickListener onRecyclerItemClickListener)
    {
        this.productList = list;
        this.context = context;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        this.type = 0;
    }
    public ProductCreationListsAdapter(final List<Product> list, final Context context, final int type,
                                       final OnRecyclerItemClickListener onRecyclerItemClickListener)
    {
        this.productList = list;
        this.context = context;
        this.type = type;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    /*public ProductCreationListsAdapter(final List<Product> list, final Context context, final int type)
    {
        this.productList = list;
        this.context = context;
        this.type = type;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_creation_list_item, parent, false);
        return new ViewHolder(view, onRecyclerItemClickListener, type, productList);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        //final Product selectedProduct = productList.get(holder.getAdapterPosition());
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
            if(type == 0)
            {
                textView_price.setText("");
            }
            else
            {
                float totalCost = product.getPrice() * product.getAmount();
                String totalCostText = String.format(Locale.getDefault(), " %s $", DecimalFormatUtils.formatCurrency(totalCost));
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
