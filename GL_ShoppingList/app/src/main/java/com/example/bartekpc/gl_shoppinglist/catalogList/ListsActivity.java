package com.example.bartekpc.gl_shoppinglist.catalogList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.DialogFactory;
import com.example.bartekpc.gl_shoppinglist.model.Product;
import com.example.bartekpc.gl_shoppinglist.productCreation.ProductAddActivity;
import com.example.bartekpc.gl_shoppinglist.productList.ProductListActivity;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Catalog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class ListsActivity extends AppCompatActivity
{
    private static final String EXTRA_CATALOG_NUMBER = "EXTRA_CATALOG_NUMBER";

    private FloatingActionMenu menu;
    private ListAdapter adapter;
    private List<Catalog> list;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_catalogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = DatabaseController.getAllCatalogs();
        adapter = new ListAdapter(list, this);
        recyclerView.setAdapter(adapter);
        menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);
        addCatalogButtonInit();
        deleteAllButtonInit();
    }

    private void deleteAllButtonInit()
    {
        final FloatingActionButton deleteAllButton = (FloatingActionButton) findViewById(R.id.menu_item_deleteAll);
        deleteAllButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                //TODO: sometimes dialog doesn't appear but catalogs are deleted anyway
                if (DatabaseController.numberOfCatalogs() > 0)
                {
                    final String text = getString(R.string.delete_all_lists_confirmation);
                    DialogFactory.getConfirmationDialog(ListsActivity.this, R.string.delete_all_lists, text, new MaterialDialog.SingleButtonCallback()
                    {
                        @Override
                        public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                        {
                            DatabaseController.deleteAllCatalogs();
                            adapter.swapList(list);
                        }
                    }).show();
                }
                menu.close(true);
            }
        });
    }

    private void addCatalogButtonInit()
    {
        final FloatingActionButton createCatalogButton = (FloatingActionButton) findViewById(R.id.menu_item_addCatalog);
        createCatalogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                View viewView = LayoutInflater.from(ListsActivity.this).inflate(R.layout.activity_list_creation, null);
                DialogFactory.getCustomViewDialog(ListsActivity.this, R.string.add_list, viewView, new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                    {
                        EditText editText = (EditText) dialog.getCustomView().findViewById(R.id.editText_listName);
                        final String userInput = editText.getText().toString();
                        if (TextUtils.isEmpty(userInput))
                        {
                            String catalogName = String.format(getString(R.string.list_plus_int), DatabaseController.getNextCatalogKey() + 1);
                            DatabaseController.addCatalog(catalogName);
                        } else
                        {
                            DatabaseController.addCatalog(userInput);
                        }
                        final Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                        intent.putExtra(EXTRA_CATALOG_NUMBER, DatabaseController.numberOfCatalogs() - 1);
                        startActivity(intent);
                    }
                }).show();
                menu.close(true);
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        adapter.swapList(list);
    }

    public void showUpdateListNameDialog(final Catalog catalog)
    {
        View listCreationView = LayoutInflater.from(ListsActivity.this).inflate(R.layout.activity_list_creation, null);
        DialogFactory.getCustomViewDialog(ListsActivity.this, R.string.change_list_name, listCreationView, new MaterialDialog.SingleButtonCallback()
        {
            @Override
            public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
            {
                EditText editText = (EditText) dialog.getCustomView().findViewById(R.id.editText_listName);
                final String userInput = editText.getText().toString();
                if (!TextUtils.isEmpty(userInput))
                {
                    DatabaseController.updateCatalogName(catalog, userInput);
                    adapter.swapList(list);
                }
            }
        }).show();
    }

    public void buildDeleteWarningDialog(final Catalog catalog)
    {
        int numberOfProducts = DatabaseController.getAllProductsInCatalog(catalog).size();
        if (numberOfProducts > 0)
        {
            final String text = String.format(getString(R.string.delete_list_confirmation), catalog.getName());
            DialogFactory.getConfirmationDialog(ListsActivity.this, R.string.delete_list, text, new MaterialDialog.SingleButtonCallback()
            {
                @Override
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                {
                    DatabaseController.deleteCatalog(catalog);
                    adapter.swapList(list);
                }
            }).show();
        } else
        {
            DatabaseController.deleteCatalog(catalog);
            adapter.swapList(list);
        }
    }
}
