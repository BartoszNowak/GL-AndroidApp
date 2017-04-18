package com.example.bartekpc.gl_shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

/**
 * Created by BartekPC on 4/6/2017.
 */

public class ProductListActivity extends AppCompatActivity
{
    ProductListAdapter adapter;
    int catalogIndex;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lista_stringow);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        Bundle extras = getIntent().getExtras();
        catalogIndex = extras.getInt("EXTRA_CATALOG_NUMBER");

        final List<Product> productList = DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(catalogIndex).getId());
        setTitle(DatabaseController.getCatalog(catalogIndex).getName());
        adapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        final com.github.clans.fab.FloatingActionButton button = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item_0);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                buildAddProductDialog();
                menu.close(true);
            }
        });
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
                if(nameInputText.matches(""))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this).create();
                    alertDialog.setTitle("Błąd");
                    alertDialog.setMessage("Nie podano nazwy produktu.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    if(priceInputText.matches("")) priceInputText = "0";
                    Product product = new Product(nameInputText, Float.parseFloat(priceInputText));
                    DatabaseController.addProduct(product, DatabaseController.getCatalog(catalogIndex).getId());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                if(nameInputText.matches(""))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductListActivity.this).create();
                    alertDialog.setTitle("Błąd");
                    alertDialog.setMessage("Nie podano nazwy produktu.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    float productPrice;
                    if(priceInputText.matches(""))
                    {
                        productPrice =  DatabaseController.getAllProductsInCatalog(DatabaseController.getCatalog(catalogIndex).getId()).get(index).getPrice();
                    }
                    else
                    {
                        productPrice = Float.parseFloat(priceInputText);
                    }
                    Product product = new Product(nameInputText, productPrice);

                    DatabaseController.updateProduct(index, DatabaseController.getCatalog(catalogIndex).getId(), product);
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

    void deleteProduct(final int index)
    {
        DatabaseController.deleteProduct(index, DatabaseController.getCatalog(catalogIndex).getId());
        adapter.notifyDataSetChanged();
    }

    void setProductPurchased(final int index, final boolean checkBoxValue)
    {
        DatabaseController.setProductPurchased(index, DatabaseController.getCatalog(catalogIndex).getId(), checkBoxValue);
    }
    void setProductFavourite(final int index, final boolean checkBoxValue)
    {
        DatabaseController.setProductFavourite(index, DatabaseController.getCatalog(catalogIndex).getId(), checkBoxValue);
}
}
