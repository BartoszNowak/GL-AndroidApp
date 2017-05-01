package com.example.bartekpc.gl_shoppinglist.productList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.DialogFactory;
import com.example.bartekpc.gl_shoppinglist.DividerItemDecoration;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;
import com.example.bartekpc.gl_shoppinglist.model.ProductFilterMode;
import com.example.bartekpc.gl_shoppinglist.model.ProductSortMode;
import com.example.bartekpc.gl_shoppinglist.productCreation.ProductAddActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class ProductListActivity extends AppCompatActivity
{
    private ProductListAdapter adapter;
    private int catalogIndex;
    private FloatingActionMenu menu;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        Bundle extras = getIntent().getExtras();
        catalogIndex = extras.getInt("EXTRA_CATALOG_NUMBER");
        final List<Product> productList = DatabaseController.getAllProductsInCatalogFiltered(DatabaseController.getCatalog(catalogIndex).getId());
        setTitle(DatabaseController.getCatalog(catalogIndex).getName());
        adapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(adapter);
        menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);
        addProductMenuButtonInit();
        sortMenuButtonInit();
        filterMenuButtonInit();
        addInitialData();
    }

    private void addProductMenuButtonInit()
    {
        final FloatingActionButton addProductButton = (FloatingActionButton) findViewById(R.id.menu_item_addProduct);
        addProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                final Intent intent = new Intent(getApplicationContext(), ProductAddActivity.class);
                intent.putExtra("EXTRA_CATALOG_ID", DatabaseController.getCatalog(catalogIndex).getId());
                startActivityForResult(intent, 1);
                menu.close(true);
            }
        });
    }

    private void sortMenuButtonInit()
    {
        final FloatingActionButton sortButton = (FloatingActionButton) findViewById(R.id.menu_item_Sort);
        sortButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                String[] values = {"Kolejności", "Nazwy", "Ceny"};
                DialogFactory.getSingleChoiceDialog(ProductListActivity.this, R.string.sort_info_text, values, DatabaseController.getSortModeIndex(), new MaterialDialog.ListCallbackSingleChoice()
                {
                    @Override
                    public boolean onSelection(final MaterialDialog dialog, final View itemView, final int which, final CharSequence text)
                    {
                        switch(which)
                        {
                            case 0:
                            {
                                DatabaseController.setSortMode(ProductSortMode.SORT_DEFAULT);
                                break;
                            }
                            case 1:
                            {
                                DatabaseController.setSortMode(ProductSortMode.SORT_BY_NAME);
                                break;
                            }
                            case 2:
                            {
                                DatabaseController.setSortMode(ProductSortMode.SORT_BY_PRICE);
                                break;
                            }
                        }
                        final List<Product> productList = DatabaseController.getAllProductsInCatalogFiltered(DatabaseController.getCatalog(catalogIndex).getId());
                        adapter.changeList(productList);
                        return true;
                    }
                }).show();
                menu.close(true);
            }
        });
    }

    private void filterMenuButtonInit()
    {
        final String[] values = {"Wszystkie", "Do kupienia"};
        final FloatingActionButton filterButton = (FloatingActionButton) findViewById(R.id.menu_item_filter);
        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                DialogFactory.getSingleChoiceDialog(ProductListActivity.this, R.string.filter_info_text, values, DatabaseController.getFilterModeIndex(), new MaterialDialog.ListCallbackSingleChoice()
                {
                    @Override
                    public boolean onSelection(final MaterialDialog dialog, final View itemView, final int which, final CharSequence text)
                    {
                        switch(which)
                        {
                            case 0:
                            {
                                DatabaseController.setFilterMode(ProductFilterMode.FILTER_ALL);
                                break;
                            }
                            case 1:
                            {
                                DatabaseController.setFilterMode(ProductFilterMode.FILTER_NOT_PURCHASED);
                                break;
                            }
                        }
                        final List<Product> productList = DatabaseController.getAllProductsInCatalogFiltered(DatabaseController.getCatalog(catalogIndex).getId());
                        adapter.changeList(productList);
                        return true;
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
        adapter.notifyDataSetChanged();
    }

    void addOrRemoveFromFavourite(final Product product)
    {
        Product favouriteProduct = DatabaseController.findProduct(product.getName());
        if(favouriteProduct != null)
        {
            DatabaseController.deleteProduct(favouriteProduct);
        }
        else
        {
            favouriteProduct = new Product(product.getName(), product.getPrice());
            favouriteProduct.setFavourite(true);
            favouriteProduct.setPurchased(false);
            DatabaseController.addProduct(favouriteProduct, -1);
        }
    }

    private void addInitialData()
    {
        String[] products = {
                getString(R.string.milk),
                getString(R.string.bread),
                getString(R.string.eggs),
                getString(R.string.potatoes),
                getString(R.string.cheese),
                getString(R.string.butter)
        };
        for(String name : products)
        {
            DatabaseController.addProduct(new Product(name), -1);
        }
    }
}
