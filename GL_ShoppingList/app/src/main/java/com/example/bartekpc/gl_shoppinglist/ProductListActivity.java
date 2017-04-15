package com.example.bartekpc.gl_shoppinglist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BartekPC on 4/6/2017.
 */

public class ProductListActivity extends AppCompatActivity
{
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

        final List<Product> productList = DatabaseController.getAllProductsInCatalog(catalogIndex);
        setTitle(DatabaseController.getCatalog(catalogIndex).getName());
        final ProductListAdapter adapter = new ProductListAdapter(productList);
        recyclerView.setAdapter(adapter);

        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setClosedOnTouchOutside(true);

        final com.github.clans.fab.FloatingActionButton button = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item_0);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                buildAddListDialog();
                menu.close(true);
            }
        });
    }

    private void buildAddListDialog()
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
                addProductToRealm(nameInput.getText().toString(), Float.parseFloat(priceInput.getText().toString()));
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

    void addProductToRealm(final String name, final float price)
    {
        Product product = new Product(name, price);
        DatabaseController.addProduct(product, catalogIndex);
    }
}
