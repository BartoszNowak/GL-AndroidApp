package com.example.bartekpc.gl_shoppinglist.catalogList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.model.Product;
import com.example.bartekpc.gl_shoppinglist.productList.ProductListActivity;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ListAdapter extends RecyclerView.Adapter
{
    private static final String EXTRA_CATALOG_NUMBER = "EXTRA_CATALOG_NUMBER";
    private static final String PRODUCT_AMOUNT_TEXT = "%1$d/%2$d";

    private final List<Catalog> catalogList = new ArrayList<>();
    private final Context context;

    ListAdapter(final List<Catalog> list, final Context context)
    {
        swapList(list);
        this.context = context;
    }

    void swapList(final List<Catalog> list)
    {
        catalogList.clear();
        catalogList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        final Catalog selectedCatalog = catalogList.get(holder.getAdapterPosition());
        ((ViewHolder) holder).bindView(catalogList.get(holder.getAdapterPosition()));
        ((ViewHolder) holder).buttonViewOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                showPopupMenu((ViewHolder) holder, selectedCatalog);
            }
        });
    }

    private void showPopupMenu(final ViewHolder holder, final Catalog selectedCatalog)
    {
        final PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
        popup.inflate(R.menu.catalog_options_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(final MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.menu1:
                        ((ListsActivity) context).showUpdateListNameDialog(selectedCatalog);
                        break;
                    case R.id.menu2:
                        ((ListsActivity) context).buildDeleteWarningDialog(selectedCatalog);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount()
    {
        return catalogList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView catalogName;
        final TextView productsAmount;
        final TextView buttonViewOption;
        private final Context context;

        ViewHolder(final View itemView)
        {
            super(itemView);
            catalogName = (TextView) itemView.findViewById(R.id.text);
            productsAmount = (TextView) itemView.findViewById(R.id.textView_amount);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }

        void bindView(Catalog catalog)
        {
            catalogName.setText(catalog.getName());
            int numberOfProducts = DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(getAdapterPosition())).size();
            int numberOfPurchasedProducts = DatabaseController.getAllPurchasedProductsInCatalog(DatabaseController.getCatalog(getAdapterPosition())).size();
            String productsNumberText = String.format(Locale.getDefault(), PRODUCT_AMOUNT_TEXT, numberOfPurchasedProducts, numberOfProducts);
            productsAmount.setText(productsNumberText);
        }

        @Override
        public void onClick(final View v)
        {
            final Intent intent = new Intent(context.getApplicationContext(), ProductListActivity.class);
            intent.putExtra(EXTRA_CATALOG_NUMBER, getAdapterPosition());
            context.startActivity(intent);
        }
    }
}
