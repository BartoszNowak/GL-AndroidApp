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
import com.example.bartekpc.gl_shoppinglist.productCreation.ProductEditActivity;
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
    private static final String EXTRA_CATALOG_NUMBER = "EXTRA_CATALOG_NUMBER";
    private static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    private static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";
    private static final int NO_CATALOG = -1;
    private static final int REQUEST_CODE = 1;
    private static final int SORT_BY_ID = 0;
    private static final int SORT_BY_NAME = 1;
    private static final int SORT_BY_PRICE = 2;
    private static final int FILTER_ALL = 0;
    private static final int FILTER_TO_BUY = 1;

    private ProductListAdapter adapter;
    private int catalogIndex;
    private FloatingActionMenu menu;
    private List<Product> productList;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        Bundle extras = getIntent().getExtras();
        catalogIndex = extras.getInt(EXTRA_CATALOG_NUMBER);
        productList = DatabaseController.getAllProductsInCatalogFiltered(DatabaseController.getCatalog(catalogIndex).getId());
        setTitle(DatabaseController.getCatalog(catalogIndex).getName());
        adapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(adapter);
        menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);
        addProductMenuButtonInit();
        sortMenuButtonInit();
        filterMenuButtonInit();
        if (DatabaseController.getAllNonFavouriteProductsFromCatalog(NO_CATALOG).size() == 0)
        {
            addInitialData();
        }
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
                intent.putExtra(EXTRA_CATALOG_ID, DatabaseController.getCatalog(catalogIndex).getId());
                startActivityForResult(intent, REQUEST_CODE);
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
                DialogFactory.getSingleChoiceDialog(ProductListActivity.this, R.string.sort_info_text, R.array.sort_types, DatabaseController.getSortModeIndex(), new MaterialDialog.ListCallbackSingleChoice()
                {
                    @Override
                    public boolean onSelection(final MaterialDialog dialog, final View itemView, final int which, final CharSequence text)
                    {
                        switch (which)
                        {
                            case SORT_BY_ID:
                            {
                                DatabaseController.setSortMode(ProductSortMode.SORT_DEFAULT);
                                break;
                            }
                            case SORT_BY_NAME:
                            {
                                DatabaseController.setSortMode(ProductSortMode.SORT_BY_NAME);
                                break;
                            }
                            case SORT_BY_PRICE:
                            {
                                DatabaseController.setSortMode(ProductSortMode.SORT_BY_PRICE);
                                break;
                            }
                        }
                        final List<Product> productList = DatabaseController.getAllProductsInCatalogFiltered(DatabaseController.getCatalog(catalogIndex).getId());
                        adapter.swapList(productList);
                        return true;
                    }
                }).show();
                menu.close(true);
            }
        });
    }

    private void filterMenuButtonInit()
    {
        final FloatingActionButton filterButton = (FloatingActionButton) findViewById(R.id.menu_item_filter);
        filterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                DialogFactory.getSingleChoiceDialog(ProductListActivity.this, R.string.filter_info_text, R.array.filter_types, DatabaseController.getFilterModeIndex(), new MaterialDialog.ListCallbackSingleChoice()
                {
                    @Override
                    public boolean onSelection(final MaterialDialog dialog, final View itemView, final int which, final CharSequence text)
                    {
                        switch (which)
                        {
                            case FILTER_ALL:
                            {
                                DatabaseController.setFilterMode(ProductFilterMode.FILTER_ALL);
                                break;
                            }
                            case FILTER_TO_BUY:
                            {
                                DatabaseController.setFilterMode(ProductFilterMode.FILTER_NOT_PURCHASED);
                                break;
                            }
                        }
                        final List<Product> productList = DatabaseController.getAllProductsInCatalogFiltered(DatabaseController.getCatalog(catalogIndex).getId());
                        adapter.swapList(productList);
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
        adapter.swapList(productList);
    }

    void addOrRemoveFromFavourite(final Product product)
    {
        Product favouriteProduct = DatabaseController.findProduct(product.getName(), NO_CATALOG);
        if (favouriteProduct != null)
        {
            DatabaseController.deleteProduct(favouriteProduct);
        } else
        {
            favouriteProduct = new Product(product.getName(), product.getPrice());
            favouriteProduct.setFavourite(true);
            favouriteProduct.setPurchased(false);
            DatabaseController.addProduct(favouriteProduct, NO_CATALOG);
        }
    }

    void startEditProductActivity(final long productId)
    {
        final Intent intent = new Intent(getApplicationContext(), ProductEditActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        startActivityForResult(intent, REQUEST_CODE);
    }

    void removeProduct(final Product product)
    {
        DatabaseController.deleteProduct(product);
        adapter.swapList(productList);
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
        for (String name : products)
        {
            DatabaseController.addProduct(new Product(name), NO_CATALOG);
        }
    }
}
