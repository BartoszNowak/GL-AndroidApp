package com.example.bartekpc.gl_shoppinglist.productList;

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

import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.DecimalFormatUtils;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter
{
    private static final int FAVOURITE_CHECKBOX_MENU_OPTION = 2;

    private List<Product> productList;
    private final Context context;

    public ProductListAdapter(final List<Product> list, final Context context)
    {
        this.productList = list;
        this.context = context;
    }

    public void changeList(final List<Product> list)
    {
        productList = list;
        notifyDataSetChanged();
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
        final Product selectedProduct = productList.get(holder.getAdapterPosition());
        ((ViewHolder) holder).bindView(productList.get(position));
        ((ViewHolder) holder).checkBox_purchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
            {
                DatabaseController.setProductPurchased(selectedProduct, isChecked);
                notifyDataSetChanged();
            }
        });
        ((ProductListAdapter.ViewHolder) holder).textView_options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                final PopupMenu popup = new PopupMenu(context, ((ProductListAdapter.ViewHolder) holder).textView_options);
                popup.inflate(R.menu.product_options_menu);
                popup.getMenu().getItem(FAVOURITE_CHECKBOX_MENU_OPTION).setChecked(selectedProduct.isFavourite());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                            {
                                //((ProductListActivity)context).buildUpdateProductDialog(holder.getAdapterPosition());
                                break;
                            }
                            case R.id.menu2:
                            {
                                DatabaseController.deleteProduct(selectedProduct);
                                notifyDataSetChanged();
                                break;
                            }
                            case R.id.menu3:
                            {
                                DatabaseController.setAllProductsWithNameFavourite(selectedProduct.getName(), !selectedProduct.isFavourite());
                                ((ProductListActivity)context).addOrRemoveFromFavourite(selectedProduct);
                                break;
                            }
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
        final TextView textView_price;
        final TextView textView_priceDetails;
        final TextView textView_options;
        final CheckBox checkBox_purchase;

        ViewHolder(final View itemView)
        {
            super(itemView);
            textView_productName = (TextView) itemView.findViewById(R.id.text);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            textView_priceDetails = (TextView) itemView.findViewById(R.id.textView_PriceDetails);
            textView_options = (TextView) itemView.findViewById(R.id.textView_options);
            checkBox_purchase = (CheckBox) itemView.findViewById(R.id.checkbox_purchase);
        }

        void bindView(final Product product)
        {
            textView_productName.setText(product.getName());
            float totalCost = product.getPrice() * product.getAmount();
            String totalCostText = String.format(Locale.getDefault(), " %s $", DecimalFormatUtils.formatCurrency(totalCost));
            textView_price.setText(totalCostText);
            String amount = DecimalFormatUtils.formatAmount(product.getAmount());
            String price = DecimalFormatUtils.formatCurrency(product.getPrice());
            String costDetails = String.format(Locale.getDefault(), "(%s x %s $)", amount, price);
            textView_priceDetails.setText(costDetails);
            checkBox_purchase.setOnCheckedChangeListener(null);
            checkBox_purchase.setChecked(product.isPurchased());
        }
    }
}
