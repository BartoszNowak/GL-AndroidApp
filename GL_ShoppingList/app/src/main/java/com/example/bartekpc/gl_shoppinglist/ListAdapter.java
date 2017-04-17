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

import java.util.List;

/**
 * Created by BartekPC on 4/6/2017.
 */



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
        ((ViewHolder) holder).bindView(catalogList.get(holder.getAdapterPosition()));
        ((ViewHolder) holder).buttonViewOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                PopupMenu popup = new PopupMenu(context, ((ViewHolder) holder).buttonViewOption);
                popup.inflate(R.menu.catalog_options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                ((ListsActivity)context).buildUpdateListNameDialog(holder.getAdapterPosition());
                                break;
                            case R.id.menu2:
                                //((ListsActivity)context).removeCatalog(holder.getAdapterPosition());
                                ((ListsActivity)context).buildDeleteWarningDialog(holder.getAdapterPosition());
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
            int numberOfProductsInCatalog = DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(getAdapterPosition()).getId()).size();
            int purchasedProducts = 0;
            StringBuilder builder = new StringBuilder();
            String productsAmountTest = String.valueOf(builder
                    .append(String.valueOf(purchasedProducts))
                    .append("/")
                    .append(String.valueOf(numberOfProductsInCatalog)));
            productsAmount.setText(productsAmountTest);
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
