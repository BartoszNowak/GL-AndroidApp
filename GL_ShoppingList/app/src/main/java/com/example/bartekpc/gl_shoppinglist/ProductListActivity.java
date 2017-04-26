package com.example.bartekpc.gl_shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bartekpc.gl_shoppinglist.model.Product;
import com.example.bartekpc.gl_shoppinglist.model.ProductFilterMode;
import com.example.bartekpc.gl_shoppinglist.model.ProductSortMode;
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
                                //final List<Product> productList = DatabaseController.getAllNotPurchasedProductsInCatalog(DatabaseController.getCatalog(catalogIndex).getId());
                                //adapter.changeList(productList);
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

    private void buildAddProductDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("Dodaj produkt");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.activity_product_creation, null);
        final EditText nameInput = (EditText) dialogView.findViewById(R.id.editText_productName);
        final EditText priceInput = (EditText) dialogView.findViewById(R.id.editText_productPrice);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                String nameInputText = nameInput.getText().toString();
                String priceInputText = priceInput.getText().toString();
                if ("".equals(nameInputText))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this).create();
                    alertDialog.setTitle("Błąd");
                    alertDialog.setMessage("Nie podano nazwy produktu.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else
                {
                    if ("".equals(priceInputText)) priceInputText = "0";
                    Product product = new Product(nameInputText, Float.parseFloat(priceInputText));
                    DatabaseController.addProduct(product, DatabaseController.getCatalog(catalogIndex).getId());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void buildUpdateProductDialog(final int index)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("Edytuj produkt");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.activity_product_creation, null);
        final EditText nameInput = (EditText) dialogView.findViewById(R.id.editText_productName);
        final EditText priceInput = (EditText) dialogView.findViewById(R.id.editText_productPrice);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
                String nameInputText = nameInput.getText().toString();
                String priceInputText = priceInput.getText().toString();
                if ("".equals(nameInputText))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this).create();
                    alertDialog.setTitle("Błąd");
                    alertDialog.setMessage("Nie podano nazwy produktu.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else
                {
                    float productPrice;
                    if ("".equals(priceInputText))
                    {
                        //productPrice =  DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(catalogIndex).getId()).get(index).getPrice();
                        //productPrice =  DatabaseController.getAllProductsInCatalog().get(index).getPrice();
                        //TODO: create activity to edit product values
                    } else
                    {
                        productPrice = Float.parseFloat(priceInputText);
                    }
                    //Product product = new Product(nameInputText, productPrice);

                    //DatabaseController.updateProduct(index, DatabaseController.getCatalog(catalogIndex).getId(), product);
                }
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
