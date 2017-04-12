package com.example.bartekpc.gl_shoppinglist;

import android.content.Context;
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
    private final List<ListCatalog> list;
    private final Context context;

    public ListAdapter(final List<ListCatalog> list, final Context context)
    {
        this.list = list;
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
        ((ViewHolder) holder).bindView(list.get(position));
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
                                ((ListsActivity)context).buildUpdateListNameDialog(position);
                                break;
                            case R.id.menu2:
                                ((ListsActivity)context).removeFromRealm(position);
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
        return list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView catalogName;
        final TextView buttonViewOption;
        ViewHolder(final View itemView)
        {
            super(itemView);
            catalogName = (TextView) itemView.findViewById(R.id.text);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }

        void bindView(ListCatalog text)
        {
            catalogName.setText(text.getName());
        }
    }
}
