package com.example.bartekpc.gl_shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.bartekpc.gl_shoppinglist.model.Catalog;

import java.util.List;
import java.util.Locale;

public class ListAdapter extends RecyclerView.Adapter
{
    private final List<Catalog> catalogList;
    private final Context context;

    public ListAdapter(final List<Catalog> catalogList, final Context context)
    {
        this.catalogList = catalogList;
        this.context = context;
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
                PopupMenu popup = new PopupMenu(context, ((ViewHolder) holder).buttonViewOption);
                popup.inflate(R.menu.catalog_options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                ((ListsActivity)context).showUpdateListNameDialog(selectedCatalog);
                                break;
                            case R.id.menu2:
                                ((ListsActivity)context).buildDeleteWarningDialog(selectedCatalog);
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
            String productsNumberText = String.format(Locale.getDefault(), "%1$d/%2$d", numberOfPurchasedProducts, numberOfProducts);
            productsAmount.setText(productsNumberText);
        }

        @Override
        public void onClick(final View v)
        {
            final Intent intent = new Intent(context.getApplicationContext(), ProductListActivity.class);
            intent.putExtra("EXTRA_CATALOG_NUMBER", getAdapterPosition());
            context.startActivity(intent);
        }
    }
}
